package anto.es.intolerables.controllers;

import anto.es.intolerables.dto.IntoleranciaDTO;
import anto.es.intolerables.entities.Intolerancia;
import anto.es.intolerables.services.IntoleranciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/intolerancias")
public class IntoleranciaController {
    private final IntoleranciaService intoleranciaService;
    @GetMapping
    public ResponseEntity<List<IntoleranciaDTO>> findAll() {
        List<IntoleranciaDTO> intolerancias = intoleranciaService.findAll()
                .stream()
                .map(IntoleranciaDTO::new)
                .toList();

        return ResponseEntity.ok(intolerancias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Intolerancia> obtenerIntoleranciaPorId(Integer id) {
        return intoleranciaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/seleccionar")
    public ResponseEntity<?> asociarIntoleranciaAlUsuario(@RequestBody Map<String, String> body, Principal principal) {
        String intoleranciaNombre = body.get("nombre");
        String nombreUsuario = principal.getName(); // suponiendo que usas JWT y email como username
        System.out.println("Usuario desde principal: " + principal.getName());
        System.out.println("Intolerancia seleccionada: " + intoleranciaNombre);
        boolean success = intoleranciaService.asociarAUsuario(intoleranciaNombre, nombreUsuario);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("No se pudo asociar la intolerancia.");
        }
    }
}
