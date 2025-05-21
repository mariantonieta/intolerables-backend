package anto.es.intolerables.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GroqService {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    @Value("${ai.api.key}")
    private String apiKey;

    private static final String MODEL = "meta-llama/llama-4-scout-17b-16e-instruct";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String getGroqResponse(String userMessage) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL);

            List<Map<String, String>> messages = List.of(Map.of(
                    "role", "user",
                    "content", userMessage
            ));
            requestBody.put("messages", messages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

            JsonNode root = mapper.readTree(response.getBody());
            return root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText(" No hubo respuesta del modelo");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al comunicarse con Groq: " + e.getMessage();
        }
    }

    public List<Map<String, String>> buscarRestaurantesIA(String intolerancia, String comida, String ubicacion) {
        try {
            String consultaUsuario = "Dame una lista de restaurantes en " + ubicacion +
                    " que sirvan " + comida + " y ofrezcan opciones para personas con " + intolerancia +
                    ". Para cada restaurante, incluye:\n\n"
                    + "- **Nombre** del restaurante\n"
                    + "- **Dirección** completa\n"
                    + "- **Tipo de comida** que sirven\n"
                    + "- **Opciones sin gluten** disponibles\n"
                    + "- **Enlace a su sitio web oficial**\n";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL);
            List<Map<String, String>> messages = List.of(Map.of(
                    "role", "user",
                    "content", consultaUsuario
            ));
            requestBody.put("messages", messages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
          //  System.out.println("Respuesta bruta de Groq:")
            //  System.out.println(response.getBody());

            JsonNode root = mapper.readTree(response.getBody());
            String respuestaIA = root.path("choices").get(0).path("message").path("content").asText();

            return parsearRespuestaAI(respuestaIA);

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(Map.of("error", "Error al comunicarse con Groq: " + e.getMessage()));
        }
    }

    private List<Map<String, String>> parsearRespuestaAI(String respuestaIA) {
        List<Map<String, String>> restaurantes = new ArrayList<>();
        Map<String, String> restauranteActual = new HashMap<>();

        String[] lineas = respuestaIA.split("\n");
        for (String linea : lineas) {
            if (linea.matches("\\d+\\.\\s\\*\\*(.*?)\\*\\*")) {
                if (!restauranteActual.isEmpty()) {
                    restauranteActual.put("id", UUID.randomUUID().toString());

                    if (!restauranteActual.containsKey("url") || !esUrlSegura(restauranteActual.get("url"))) {
                        String nombre = restauranteActual.getOrDefault("nombre", "").replace(" ", "+");
                        String ubicacion = restauranteActual.getOrDefault("direccion", "").replace(" ", "+");
                        restauranteActual.put("url", "https://www.google.com/search?q=Restaurante+" + nombre + "+" + ubicacion);
                    }

                    restaurantes.add(restauranteActual);
                    restauranteActual = new HashMap<>();
                }
                restauranteActual.put("nombre", limpiarTexto(linea));
            } else if (linea.contains("Dirección:")) {
                restauranteActual.put("direccion", limpiarTexto(linea));
            } else if (linea.contains("Tipo de comida:")) {
                restauranteActual.put("tipo_comida", limpiarTexto(linea));
            } else if (linea.contains("Opciones sin gluten:")) {
                restauranteActual.put("opciones_sin_gluten", limpiarTexto(linea));
            } else if (linea.contains("Enlace a su sitio web oficial:") || linea.contains("Sitio web:") || linea.contains("URL:")) {
                String url = extraerUrl(linea);
                restauranteActual.put("url", esUrlSegura(url) ? url : "");
            }
        }

        if (!restauranteActual.isEmpty()) {
            restauranteActual.put("id", UUID.randomUUID().toString());

            if (!restauranteActual.containsKey("url") || !esUrlSegura(restauranteActual.get("url"))) {
                String nombre = restauranteActual.getOrDefault("nombre", "").replace(" ", "+");
                String ubicacion = restauranteActual.getOrDefault("direccion", "").replace(" ", "+");
                restauranteActual.put("url", "https://www.google.com/search?q=Restaurante+" + nombre + "+" + ubicacion);
            }

            restaurantes.add(restauranteActual);
        }

        return restaurantes;
    }



    private String limpiarTexto(String texto) {
        return texto.replaceAll("\\*+", "")
                .replaceAll("Dirección:", "")
                .replaceAll("Tipo de comida:", "")
                .replaceAll("Opciones sin gluten:", "")
                .trim();
    }

    private String extraerUrl(String texto) {
        return texto.replaceAll(".*?\\[(.*?)\\]\\((https?://[^)]+)\\)", "$2").trim();
    }



    private boolean esUrlSegura(String url) {
        return url != null && url.startsWith("https://") && !url.contains(" ");
    }
}
