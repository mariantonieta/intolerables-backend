package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.FavoritoRestauranteDTO;
import anto.es.intolerables.entities.FavoritoRestaurante;
import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.FavoritoRestauranteRepository;
import anto.es.intolerables.repositories.RestauranteRepository;
import anto.es.intolerables.services.FavoritoRestauranteService;
import anto.es.intolerables.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/favoritos-restaurantes")
public class FavoritoRestauranteController { private final FavoritoRestauranteService favoritoRestauranteService;
    private final UsuarioService usuarioService;
    private final RestauranteRepository restauranteRepository;
    private final FavoritoRestauranteRepository favoritoRepo;


    @GetMapping

    public ResponseEntity<List<FavoritoRestauranteDTO>> getFavoritos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = authentication.getName();

        Usuario usuario = usuarioService.findByNombre(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<FavoritoRestaurante> favoritos = favoritoRepo.findByUsuario(usuario);
        List<FavoritoRestauranteDTO> dtoList = favoritos.stream()
                .map(FavoritoRestauranteDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearFavorito(@Valid @RequestBody FavoritoRestaurante favorito) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nombreUsuario = authentication.getName();

            Usuario usuario = usuarioService.findByNombre(nombreUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (favorito.getRestaurante() == null || favorito.getRestaurante().getId() == null) {
                throw new RuntimeException("El objeto restaurante o su ID es nulo en la petición");
            }

            Restaurante restaurante = restauranteRepository.findById(favorito.getRestaurante().getId())
                    .orElseThrow(() -> new RuntimeException("El restaurante no existe en la base de datos"));

            favorito.setUsuario(usuario);
            favorito.setRestaurante(restaurante);

            FavoritoRestaurante f = favoritoRestauranteService.save(favorito);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", f.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Integer id) {
        try {
            if (favoritoRestauranteService.findById(id).isPresent()) {
                favoritoRestauranteService.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }}