package anto.es.intolerables.services;
import anto.es.intolerables.entities.FavoritoRestaurante;
import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.FavoritoRestauranteRepository;
import anto.es.intolerables.repositories.RestauranteRepository;
import anto.es.intolerables.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FavoritoRestauranteServiceTest {

    @Mock
    private FavoritoRestauranteRepository favoritoRepo;

    @Mock
    private UsuarioRepository usuarioRepo;

    @Mock
    private RestauranteRepository restauranteRepo;

    @InjectMocks
    private FavoritoRestauranteService favoritoRestauranteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe retornar todos los favoritos de restaurante")
    void testFindAll() {
        FavoritoRestaurante fr1 = new FavoritoRestaurante();
        FavoritoRestaurante fr2 = new FavoritoRestaurante();

        when(favoritoRepo.findAll()).thenReturn(Arrays.asList(fr1, fr2));

        List<FavoritoRestaurante> resultado = favoritoRestauranteService.findAll();

        assertThat(resultado).hasSize(2);
        verify(favoritoRepo).findAll();
    }

    @Test
    @DisplayName("Debe buscar favorito por usuario y restaurante")
    void testFindByUsuarioAndRestaurante() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Restaurante restaurante = new Restaurante();
        restaurante.setId(2);

        FavoritoRestaurante favorito = new FavoritoRestaurante();
        favorito.setUsuario(usuario);
        favorito.setRestaurante(restaurante);

        when(favoritoRepo.findByUsuarioAndRestaurante(usuario, restaurante)).thenReturn(Optional.of(favorito));

        Optional<FavoritoRestaurante> resultado = favoritoRestauranteService.findByUsuarioAndRestaurante(usuario, restaurante);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getUsuario().getId()).isEqualTo(1);
        assertThat(resultado.get().getRestaurante().getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe buscar favorito por ID")
    void testFindById() {
        FavoritoRestaurante favorito = new FavoritoRestaurante();
        favorito.setId(1);

        when(favoritoRepo.findById(1)).thenReturn(Optional.of(favorito));

        Optional<FavoritoRestaurante> resultado = favoritoRestauranteService.findById(1);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe guardar favorito con fecha actual")
    void testSave() {
        FavoritoRestaurante favorito = new FavoritoRestaurante();
        favorito.setId(10);

        when(favoritoRepo.save(any(FavoritoRestaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FavoritoRestaurante resultado = favoritoRestauranteService.save(favorito);

        assertThat(resultado.getFecha()).isEqualTo(LocalDate.now());
        verify(favoritoRepo).save(favorito);
    }

    @Test
    @DisplayName("Debe eliminar favorito por ID")
    void testDeleteById() {
        favoritoRestauranteService.deleteById(3);
        verify(favoritoRepo).deleteById(3);
    }
}
