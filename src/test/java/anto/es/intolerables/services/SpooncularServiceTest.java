package anto.es.intolerables.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SpooncularServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TraduccionService traduccionService;

    @InjectMocks
    private SpooncularService spooncularService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        spooncularService.setSpooncularApiKey("FAKE_API_KEY");
    }

    @Test
    void buscarRecetasPorIntolerancia_devuelveListaDeRecetasTraducidas() {

        Map<String, Object> recetaSimulada = new HashMap<>();
        recetaSimulada.put("id", 123);
        recetaSimulada.put("title", "Pancakes");
        recetaSimulada.put("image", "img.jpg");
        recetaSimulada.put("readyInMinutes", 20);
        recetaSimulada.put("summary", "Delicious Pancakes with 300 calories");

        List<Map<String, Object>> results = List.of(recetaSimulada);
        Map<String, Object> responseMock = new HashMap<>();
        responseMock.put("results", results);

        // Ingredientes y pasos simulados
        Map<String, Object> detalle = new HashMap<>();
        detalle.put("extendedIngredients", List.of(Map.of("original", "2 eggs")));
        detalle.put("analyzedInstructions", List.of(Map.of("steps", List.of(Map.of("number", 1, "step", "Mix everything")))));
        when(restTemplate.getForObject(contains("complexSearch"), eq(Map.class)))
                .thenReturn(responseMock);

        when(restTemplate.getForObject(contains("/information"), eq(Map.class)))
                .thenReturn(detalle);

        when(traduccionService.traducirTextoLibreTranslate(anyString(), eq("en"), eq("es")))
                .thenAnswer(i -> i.getArgument(0) + " (es)");

        List<Map<String, Object>> recetas = spooncularService.buscarRecetasPorIntolerancia("gluten", "pancake", "es");

        assertThat(recetas).hasSize(1);
        Map<String, Object> receta = recetas.get(0);
        assertThat(receta.get("title")).isEqualTo("Pancakes (es)");
        assertThat(receta.get("summary")).isEqualTo("Delicious Pancakes with 300 calories (es)");

        List<Map<String, String>> ingredientes = (List<Map<String, String>>) receta.get("extendedIngredients");
        assertThat(ingredientes.get(0).get("original")).contains("2 eggs");

        List<Map<String, Object>> instrucciones = (List<Map<String, Object>>) receta.get("analyzedInstructions");
        Map<String, Object> paso = ((List<Map<String, Object>>) instrucciones.get(0).get("steps")).get(0);
        assertThat(paso.get("step").toString()).contains("Mix everything");
    }


    @Test
    void buscarRecetasPorIntolerancia_manejaPasosNulos() {
        Map<String, Object> recetaSimulada = new HashMap<>();
        recetaSimulada.put("id", 999);
        recetaSimulada.put("title", "Rice");
        recetaSimulada.put("image", "img.jpg");
        recetaSimulada.put("readyInMinutes", 15);
        recetaSimulada.put("summary", "Rice summary with 100 calories");

        List<Map<String, Object>> results = List.of(recetaSimulada);
        Map<String, Object> responseMock = Map.of("results", results);

        Map<String, Object> detalle = new HashMap<>();
        detalle.put("extendedIngredients", List.of(Map.of("original", "1 cup of rice")));
        detalle.put("analyzedInstructions", null);

        when(restTemplate.getForObject(contains("complexSearch"), eq(Map.class)))
                .thenReturn(responseMock);

        when(restTemplate.getForObject(contains("/information"), eq(Map.class)))
                .thenReturn(detalle);

        when(traduccionService.traducirTextoLibreTranslate(anyString(), eq("en"), eq("es")))
                .thenAnswer(i -> i.getArgument(0) + " (es)");

        List<Map<String, Object>> recetas = spooncularService.buscarRecetasPorIntolerancia("gluten", "rice", "es");

        List<Map<String, Object>> instrucciones = (List<Map<String, Object>>) recetas.get(0).get("analyzedInstructions");
        assertThat(instrucciones).hasSize(1);
        assertThat(instrucciones.get(0).get("steps").toString()).contains("Sin pasos disponibles");
    }
}
