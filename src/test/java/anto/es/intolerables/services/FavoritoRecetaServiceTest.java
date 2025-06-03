package anto.es.intolerables.services;
import anto.es.intolerables.entities.FavoritoReceta;
import anto.es.intolerables.repositories.FavoritoRecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FavoritoRecetaServiceTest {

    @Mock
    private FavoritoRecetaRepository favoritoRecetaRepository;

    @InjectMocks
    private FavoritoRecetaService favoritoRecetaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe retornar todos los favoritos de recetas")
    void testFindAll() {
        FavoritoReceta fr1 = new FavoritoReceta();
        FavoritoReceta fr2 = new FavoritoReceta();
        when(favoritoRecetaRepository.findAll()).thenReturn(Arrays.asList(fr1, fr2));

        List<FavoritoReceta> resultado = favoritoRecetaService.findAll();

        assertThat(resultado).hasSize(2);
        verify(favoritoRecetaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar un favorito por ID")
    void testFindById() {
        FavoritoReceta favorito = new FavoritoReceta();
        favorito.setId(1);

        when(favoritoRecetaRepository.findById(1)).thenReturn(Optional.of(favorito));

        Optional<FavoritoReceta> resultado = favoritoRecetaService.findById(1);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1);
        verify(favoritoRecetaRepository).findById(1);
    }

    @Test
    @DisplayName("Debe guardar un favorito")
    void testSave() {
        FavoritoReceta favorito = new FavoritoReceta();
        favorito.setId(1);

        when(favoritoRecetaRepository.save(favorito)).thenReturn(favorito);

        FavoritoReceta resultado = favoritoRecetaService.save(favorito);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        verify(favoritoRecetaRepository).save(favorito);
    }

    @Test
    @DisplayName("Debe eliminar un favorito por ID")
    void testDeleteById() {
        Integer id = 1;

        favoritoRecetaService.deleteById(id);

        verify(favoritoRecetaRepository, times(1)).deleteById(id);
    }
}
