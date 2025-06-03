package anto.es.intolerables.services;

import anto.es.intolerables.dto.*;
import anto.es.intolerables.entities.*;
import anto.es.intolerables.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private IngredienteRepository ingredienteRepository;

    @Mock
    private TraduccionService traduccionService;

    @InjectMocks
    private RecetaService recetaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearReceta_guardarYDevuelveDTO() {
        RecetaDTO dto = new RecetaDTO();
        dto.setTitle("Tortilla");
        dto.setSummary("Receta de tortilla española");
        dto.setCalories(200);
        dto.setReadyInMinutes(30);
        dto.setImage("image.jpg");

        PasoPreparacionDTO pasoDTO = new PasoPreparacionDTO("Batir los huevos");
        dto.setPasosPreparacion(List.of(pasoDTO));

        IngredienteDTO ingredienteDTO = new IngredienteDTO(null, "Huevos", "2 unidades");
        dto.setRecetaIngredientes(List.of(ingredienteDTO));

        ArgumentCaptor<Receta> recetaCaptor = ArgumentCaptor.forClass(Receta.class);
        when(recetaRepository.save(any(Receta.class))).thenAnswer(i -> i.getArgument(0));

        RecetaDTO resultado = recetaService.crearReceta(dto);

        assertThat(resultado.getTitle()).isEqualTo("Tortilla");
        assertThat(resultado.getPasosPreparacion()).hasSize(1);
        assertThat(resultado.getRecetaIngredientes()).hasSize(1);
        verify(recetaRepository).save(recetaCaptor.capture());
    }

    @Test
    void obtenerTodasLasRecetas_devuelveListaDTOs() {
        Receta receta = new Receta();
        receta.setId(1);
        receta.setTitle("Gazpacho");
        receta.setSummary("Sopa fría andaluza");

        when(recetaRepository.findAll()).thenReturn(List.of(receta));

        List<RecetaDTO> resultado = recetaService.obtenerTodasLasRecetas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitle()).isEqualTo("Gazpacho");
    }

    @Test
    void convertirDesdeSpoonacular_devuelveEntidadReceta() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("title", "Ensalada");
        datos.put("image", "ensalada.jpg");
        datos.put("readyInMinutes", 15);
        datos.put("calories", 100);

        List<Map<String, Object>> ingredientes = List.of(
                Map.of("name", "Lechuga", "amount", 1.0, "unit", "cabeza"),
                Map.of("name", "Tomate", "amount", 2, "unit", "unidad")
        );
        datos.put("extendedIngredients", ingredientes);

        List<Map<String, Object>> pasos = List.of(
                Map.of("step", "Lavar la lechuga"),
                Map.of("step", "Cortar el tomate")
        );
        datos.put("analyzedInstructions", List.of(Map.of("steps", pasos)));

        Receta receta = recetaService.convertirDesdeSpoonacular(datos);

        assertThat(receta.getIngredientes()).hasSize(2);
        assertThat(receta.getPasosPreparacion()).hasSize(2);
        assertThat(receta.getTitle()).isEqualTo("Ensalada");
    }

    @Test
    void traducirRecetaDTO_traduceTodosLosCampos() {
        RecetaDTO dto = new RecetaDTO();
        dto.setTitle("Tarta");
        dto.setSummary("Postre dulce");
        dto.setRecetaIngredientes(List.of(new IngredienteDTO(1, "Harina", "200 gramos")));
        dto.setPasosPreparacion(List.of(new PasoPreparacionDTO("Mezclar los ingredientes")));

        when(traduccionService.traducirTextoLibreTranslate(anyString(), eq("es"), eq("en")))
                .thenAnswer(i -> i.getArgument(0) + " (en)");

        RecetaDTO resultado = recetaService.traducirRecetaDTO(dto, "en");

        assertThat(resultado.getTitle()).endsWith("(en)");
        assertThat(resultado.getRecetaIngredientes().get(0).getNombre()).contains("Harina");
        assertThat(resultado.getPasosPreparacion().get(0).getDescripcion()).contains("Mezclar");
    }
}
