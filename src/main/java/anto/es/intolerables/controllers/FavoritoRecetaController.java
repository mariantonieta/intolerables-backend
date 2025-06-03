package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.FavoritoRecetaDTO;
import anto.es.intolerables.entities.FavoritoReceta;
import anto.es.intolerables.entities.Receta;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.FavoritoRecetaRepository;
import anto.es.intolerables.repositories.RecetaRepository;
import anto.es.intolerables.services.FavoritoRecetaService;
import anto.es.intolerables.services.RecetaService;
import anto.es.intolerables.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/favoritos-recetas")
public class FavoritoRecetaController {
    private final FavoritoRecetaService favoritoRecetaService;
    private final UsuarioService usuarioService;
    private final RecetaRepository recetaRepository;
    private final FavoritoRecetaRepository favoritoRepo;
    private final RecetaService recetaService;


    @GetMapping
    public ResponseEntity<List<FavoritoRecetaDTO>> getFavoritos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = authentication.getName();

        Usuario usuario = usuarioService.findByNombre(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<FavoritoReceta> favoritos = favoritoRepo.findByUsuario(usuario);
        List<FavoritoRecetaDTO> dtoList = favoritos.stream()
                .map(FavoritoRecetaDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearFavorito(@Valid @RequestBody FavoritoReceta favorito) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nombreUsuario = authentication.getName();
            System.out.println("Usuario autenticado: " + nombreUsuario);

            Usuario usuario = usuarioService.findByNombre(nombreUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (favorito.getReceta() == null) {
                throw new RuntimeException("El objeto receta es nulo en la petición");
            }
            if (favorito.getReceta().getId() == null) {
                favorito.getReceta().setId(generarNuevoId());
            }

            Receta receta = recetaRepository.findById(favorito.getReceta().getId())
                    .orElseGet(() -> {
                        Receta nuevaReceta = favorito.getReceta();
                        nuevaReceta.setId(generarNuevoId());
                        return recetaRepository.save(nuevaReceta);
                    });
            System.out.println("Receta recibida: " + favorito.getReceta());

            favorito.setUsuario(usuario);
            favorito.setReceta(receta);

            FavoritoReceta f = favoritoRecetaService.save(favorito);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", f.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    private Integer generarNuevoId() {
        return Math.abs((int) System.currentTimeMillis());
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Integer id) {
        if (favoritoRecetaService.findById(id).isPresent()) {
            favoritoRecetaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/spoonacular")
    @Transactional
    public ResponseEntity<?> crearFavoritoDesdeSpoonacular(@RequestBody Map<String, Object> datosReceta) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nombreUsuario = authentication.getName();

            Usuario usuario = usuarioService.findByNombre(nombreUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


            Receta receta = recetaService.convertirDesdeSpoonacular(datosReceta);

            Receta recetaGuardada = recetaRepository.findByTitle(receta.getTitle())
                    .orElseGet(() -> recetaRepository.save(receta)); // Guardar la receta si no existe

            FavoritoReceta favorito = new FavoritoReceta();
            favorito.setUsuario(usuario);
            favorito.setReceta(recetaGuardada);
            FavoritoReceta favoritoGuardado = favoritoRecetaService.save(favorito);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id", favoritoGuardado.getId(),
                    "mensaje", "Receta de Spoonacular guardada como favorita correctamente"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
