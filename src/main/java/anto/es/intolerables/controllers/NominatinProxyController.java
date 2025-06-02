package anto.es.intolerables.controllers;
import anto.es.intolerables.dto.CordenadasRequestDTO;
import org.hibernate.boot.model.internal.CollectionSecondPass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@RestController
public class NominatinProxyController {

    @GetMapping("/api/nominatim/search")
    public ResponseEntity<String> buscarDireccion(@RequestParam String q) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://nominatim.openstreetmap.org/search?q=" + q + "&format=json&limit=1";

        // Crear encabezados para cumplir con requisitos de Nominatim (User-Agent)
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "TuAppNombre/1.0 (tuemail@ejemplo.com)");

        // Hacer la petición GET
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.getBody());
    }
    @PostMapping("/api/nominatim/reverse")
    public ResponseEntity<Map<String, String>> obtenerCiudad(@RequestBody CordenadasRequestDTO coordenadas) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=%f&lon=%f",
                    coordenadas.getLatitude(), coordenadas.getLongitude()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "TuAppNombre/1.0 (tuemail@ejemplo.com)");

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("address")) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Dirección no encontrada"));
            }

            Map<String, Object> address = (Map<String, Object>) body.get("address");

            String ciudad = (String) (
                    address.get("city") != null ? address.get("city") :
                            address.get("town") != null ? address.get("town") :
                                    address.get("village") != null ? address.get("village") :
                                            ""
            );

            return ResponseEntity.ok(Collections.singletonMap("ciudad", ciudad));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Error al obtener ciudad"));
        }}
}
