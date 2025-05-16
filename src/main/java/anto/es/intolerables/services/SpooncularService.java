package anto.es.intolerables.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SpooncularService {

    @Value("${spoonacular.api.key}")
    private String spooncularApiKey;

    private static final String BASE_URL = "https://api.spoonacular.com/recipes/complexSearch";

    private final RestTemplate restTemplate;

    private final TraduccionService traduccionService;

    @Autowired
    public SpooncularService(RestTemplate restTemplate, TraduccionService traduccionService) {
        this.restTemplate = restTemplate;
        this.traduccionService = traduccionService;
    }

    public void setSpooncularApiKey(String apiKey) {
        this.spooncularApiKey = apiKey;
    }
    public List<Map<String, Object>> buscarRecetasPorIntolerancia(String intolerancia, String query, String idiomaDestino) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("intolerances", intolerancia)
                .queryParam("addRecipeInformation", "true")
                .queryParam("number", 10)
                .queryParam("apiKey", spooncularApiKey);

        if (query != null && !query.trim().isEmpty()) {
            builder.queryParam("query", query);
        }

        String url = builder.toUriString();
        System.out.println("URL SPOONACULAR: " + url);

        Map response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        List<Map<String, Object>> recetasList = new ArrayList<>();

        for (Map<String, Object> item : results) {
            int recetaId = (int) item.get("id");
            String detalleUrl = "https://api.spoonacular.com/recipes/" + recetaId + "/information?includeNutrition=false&apiKey=" + spooncularApiKey;
            Map detalle = restTemplate.getForObject(detalleUrl, Map.class);

            Map<String, Object> recetaMap = new HashMap<>();

            // Traducir título (origen: en, destino: idiomaDestino)
            recetaMap.put("title", traduccionService.traducirTextoLibreTranslate((String) item.get("title"), "en", idiomaDestino));
            recetaMap.put("image", item.get("image"));
            recetaMap.put("readyInMinutes", item.getOrDefault("readyInMinutes", 0));
            recetaMap.put("calories", extraerCaloriasDesdeSummary((String) item.get("summary")));

            // Traducir resumen (origen: en, destino: idiomaDestino)
            String summary = (String) item.get("summary");
            recetaMap.put("summary", traduccionService.traducirTextoLibreTranslate(summary, "en", idiomaDestino));

            // Traducir ingredientes
            List<Map<String, String>> ingredientesList = new ArrayList<>();
            List<Map<String, Object>> ingredientesRaw = (List<Map<String, Object>>) detalle.get("extendedIngredients");
            if (ingredientesRaw != null) {
                for (Map<String, Object> ing : ingredientesRaw) {
                    Map<String, String> ingMap = new HashMap<>();
                    String original = (String) ing.get("original");
                    // Traducir el nombre del ingrediente (origen: en, destino: idiomaDestino)
                    ingMap.put("original", traduccionService.traducirTextoLibreTranslate(original, "en", idiomaDestino));
                    ingredientesList.add(ingMap);
                }
            }
            recetaMap.put("extendedIngredients", ingredientesList);

            // Traducir instrucciones
            List<Map<String, Object>> instruccionesList = new ArrayList<>();
            List<Map<String, Object>> instruccionesRaw = (List<Map<String, Object>>) detalle.get("analyzedInstructions");
            if (instruccionesRaw != null && !instruccionesRaw.isEmpty()) {
                Map<String, Object> primera = instruccionesRaw.get(0);
                List<Map<String, Object>> steps = (List<Map<String, Object>>) primera.get("steps");
                List<Map<String, Object>> pasosTraducidos = new ArrayList<>();

                for (Map<String, Object> paso : steps) {
                    String stepText = (String) paso.get("step");
                    Map<String, Object> pasoTraducido = new HashMap<>();
                    pasoTraducido.put("number", paso.get("number"));
                    // Traducir paso de instrucción (origen: en, destino: idiomaDestino)
                    pasoTraducido.put("step", traduccionService.traducirTextoLibreTranslate(stepText, "en", idiomaDestino));
                    pasosTraducidos.add(pasoTraducido);
                }

                instruccionesList.add(Map.of("steps", pasosTraducidos));
            } else {
                instruccionesList.add(Map.of("steps", List.of(Map.of("step", traduccionService.traducirTextoLibreTranslate("Sin pasos disponibles", "en", idiomaDestino)))));
            }

            recetaMap.put("analyzedInstructions", instruccionesList);
            recetasList.add(recetaMap);
        }

        return recetasList;
    }

    private int extraerCaloriasDesdeSummary(String summary) {
        if (summary == null) return 0;
        Pattern pattern = Pattern.compile("(\\d+) calories");
        Matcher matcher = pattern.matcher(summary.toLowerCase());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

}
