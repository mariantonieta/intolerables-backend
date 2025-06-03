package anto.es.intolerables.services;

import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.repositories.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RestauranteServiceTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private GroqService groqService;

    @InjectMocks
    private RestauranteService restauranteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsListOfRestaurantes() {
        List<Restaurante> lista = List.of(new Restaurante(), new Restaurante());
        when(restauranteRepository.findAll()).thenReturn(lista);

        List<Restaurante> result = restauranteService.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void findById_returnsRestauranteIfExists() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1);

        when(restauranteRepository.findById(1)).thenReturn(Optional.of(restaurante));

        Optional<Restaurante> result = restauranteService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    void save_persistsRestaurante() {
        Restaurante r = new Restaurante();
        when(restauranteRepository.save(r)).thenReturn(r);

        Restaurante saved = restauranteService.save(r);
        assertThat(saved).isNotNull();
    }

    @Test
    void buscarRestaurantesConIA_returnsFromGroqService() {
        List<Map<String, String>> mockIA = List.of(
                Map.of("nombre", "Veggie Place", "direccion", "Calle Falsa 123", "url", "https://veggie.com")
        );
        when(groqService.buscarRestaurantesIA("gluten", "vegano", "Madrid")).thenReturn(mockIA);

        List<Map<String, String>> result = restauranteService.buscarRestaurantesConIA("gluten", "vegano", "Madrid");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).get("nombre")).isEqualTo("Veggie Place");
    }

    @Test
    void guardarRestaurantesEnBD_savesParsedList() {
        Map<String, String> mapa = Map.of(
                "nombre", "Restaurante Uno",
                "direccion", "Av. Siempre Viva 742",
                "url", "https://restaurante1.com"
        );

        Restaurante mockSaved = new Restaurante();
        mockSaved.setId(100);
        mockSaved.setNombre("Restaurante Uno");

        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(mockSaved);

        List<Restaurante> result = restauranteService.guardarRestaurantesEnBD(List.of(mapa));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(100);
        assertThat(result.get(0).getNombre()).isEqualTo("Restaurante Uno");
    }
}
