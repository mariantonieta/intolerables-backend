package anto.es.intolerables.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Service
public class TraduccionService {
    RestTemplate restTemplate = new RestTemplate();


    public String traducirTextoLibreTranslate(String textoOriginal, String idiomaOrigen,  String idiomaDestino) {
        String url = "http://localhost:5000/translate";
        if (idiomaOrigen.equals(idiomaDestino)) {
            return textoOriginal;
        }
        Map<String, String> body = new HashMap<>();
        body.put("q", textoOriginal);
        body.put("source", idiomaOrigen);
        body.put("target", idiomaDestino);
        body.put("format", "text");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            Map<String, String> response = restTemplate.postForObject(url, request, Map.class);
            return response != null ? response.get("translatedText") : textoOriginal;
        } catch (Exception e) {
            System.err.println("Error al traducir: " + e.getMessage());
            return textoOriginal;
        }
    }
}