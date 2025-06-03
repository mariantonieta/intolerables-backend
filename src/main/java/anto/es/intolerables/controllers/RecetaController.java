package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.RecetaDTO;
import anto.es.intolerables.entities.Receta;
import anto.es.intolerables.repositories.RecetaRepository;
import anto.es.intolerables.services.RecetaService;
import anto.es.intolerables.services.SpooncularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaService recetaService;
    private final SpooncularService recetaSpooncularService;
    private final RecetaRepository recetaRepository;


    @GetMapping("/buscar")
    public ResponseEntity<?> buscarRecetasPorIntoleranciaYNombre(
            @RequestParam String intolerancia,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "es") String lang) {
        List<Map<String, Object>> recetas = recetaSpooncularService.buscarRecetasPorIntolerancia(intolerancia, query, lang);

        return ResponseEntity.ok(Map.of("results", recetas));
    }

    @PostMapping
    public ResponseEntity<RecetaDTO> crearReceta(@RequestBody RecetaDTO recetaDTO) {
        RecetaDTO nuevaReceta = recetaService.crearReceta(recetaDTO);
        return ResponseEntity.ok(nuevaReceta);
    }
    @GetMapping("/todas")
    public ResponseEntity<List<RecetaDTO>> obtenerRecetas(@RequestParam(required = false, defaultValue = "es") String idiomaDestino) {
        // Obtener todas las recetas desde el servicio
        List<RecetaDTO> recetas = recetaService.obtenerTodasLasRecetas();

        // Traducir las recetas si se necesita
        List<RecetaDTO> recetasTraducidas = new ArrayList<>();
        for (RecetaDTO recetaDTO : recetas) {
            // Traducir cada receta usando el servicio de traducción y obtener la receta traducida como RecetaDTO
            RecetaDTO recetaTraducida = recetaService.traducirRecetaDTO(recetaDTO, idiomaDestino);
            recetasTraducidas.add(recetaTraducida);
        }

        return ResponseEntity.ok(recetasTraducidas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReceta(@PathVariable Integer id) {
        try {
            // Verificar si la receta existe antes de eliminar
            if (recetaRepository.findById(id).isPresent()) {
                recetaRepository.deleteById(id);
                return ResponseEntity.noContent().build(); // Código 204: Eliminación exitosa
            } else {
                return ResponseEntity.notFound().build(); // Código 404: No existe la receta
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarRecetaDesdeSpoonacular(@RequestBody Map<String, Object> datosReceta) {
        try {
            Receta receta = recetaService.convertirDesdeSpoonacular(datosReceta);

            Receta recetaGuardada = recetaRepository.findByTitle(receta.getTitle())
                    .orElseGet(() -> recetaRepository.save(receta));

            return ResponseEntity.ok(Map.of("id", recetaGuardada.getId()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
