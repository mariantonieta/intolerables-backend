package anto.es.intolerables.controllers;

import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.RestauranteRepository;
import anto.es.intolerables.services.RestauranteService;
import anto.es.intolerables.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurantes")

public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final RestauranteService restauranteService;
    private final UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<List<Restaurante>> listarRestaurantes() {
        try {
            return ResponseEntity.ok(restauranteService.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<Restaurante>> buscarRestaurantes(
            @RequestParam String intolerancia,
            @RequestParam String comida,
            @RequestParam String ubicacion) {

        List<Map<String, String>> resultado = restauranteService.buscarRestaurantesConIA(intolerancia, comida, ubicacion);


        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Restaurante> restaurantesGuardados = restauranteService.guardarRestaurantesEnBD(resultado);


        return ResponseEntity.ok(restaurantesGuardados);
    }


    }