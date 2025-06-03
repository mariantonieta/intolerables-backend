package anto.es.intolerables.services;

import anto.es.intolerables.entities.Intolerancia;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.entities.UsuarioIntolerancia;
import anto.es.intolerables.repositories.IntoleranciaRepository;
import anto.es.intolerables.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IntoleranciaServiceTest {

    @InjectMocks
    private IntoleranciaService intoleranciaService;

    @Mock
    private IntoleranciaRepository intoleranciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsAllIntolerancias() {
        List<Intolerancia> intolerancias = List.of(new Intolerancia(), new Intolerancia());
        when(intoleranciaRepository.findAll()).thenReturn(intolerancias);

        List<Intolerancia> result = intoleranciaService.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void findById_returnsIntolerancia() {
        Intolerancia intolerancia = new Intolerancia();
        intolerancia.setId(1);
        when(intoleranciaRepository.findById(1)).thenReturn(Optional.of(intolerancia));

        Optional<Intolerancia> result = intoleranciaService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    void save_savesIntolerancia() {
        Intolerancia intolerancia = new Intolerancia();
        when(intoleranciaRepository.save(intolerancia)).thenReturn(intolerancia);

        Intolerancia result = intoleranciaService.save(intolerancia);
        assertThat(result).isNotNull();
    }

    @Test
    void asociarAUsuario_successfullyAssociatesIntolerancia() {
        Usuario usuario = new Usuario();
        usuario.setNombre("juan");
        usuario.setIntolerancias(new ArrayList<>());

        Intolerancia intolerancia = new Intolerancia();
        intolerancia.setNombre("gluten");

        when(usuarioRepository.findByNombre("juan")).thenReturn(Optional.of(usuario));
        when(intoleranciaRepository.findByNombreContainingIgnoreCase("gluten")).thenReturn(Optional.of(intolerancia));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        boolean result = intoleranciaService.asociarAUsuario("gluten", "juan");

        assertThat(result).isTrue();
        assertThat(usuario.getIntolerancias()).hasSize(1);
        assertThat(usuario.getIntolerancias().get(0).getIntolerancia()).isEqualTo(intolerancia);
    }

    @Test
    void asociarAUsuario_returnsFalseWhenNotFound() {
        when(usuarioRepository.findByNombre("juan")).thenReturn(Optional.empty());

        boolean result = intoleranciaService.asociarAUsuario("gluten", "juan");
        assertThat(result).isFalse();
    }

    @Test
    void findAllById_returnsMatchingIntolerancias() {
        Intolerancia i1 = new Intolerancia();
        i1.setId(1);
        Intolerancia i2 = new Intolerancia();
        i2.setId(2);
        List<Intolerancia> list = List.of(i1, i2);

        when(intoleranciaRepository.findByIdIn(List.of(1, 2))).thenReturn(list);

        List<Intolerancia> result = intoleranciaService.findAllById(List.of(1, 2));
        assertThat(result).hasSize(2);
    }

    @Test
    void actualizarIntoleranciaUsuario_updatesSuccessfully() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Intolerancia intolerancia = new Intolerancia();
        intolerancia.setId(10);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(intoleranciaRepository.findById(10)).thenReturn(Optional.of(intolerancia));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        boolean result = intoleranciaService.actualizarIntoleranciaUsuario(1, 10);
        assertThat(result).isTrue();
        assertThat(usuario.getIntoleranciaSeleccionada()).isEqualTo(intolerancia);
    }

    @Test
    void actualizarIntoleranciaUsuario_removesIntolerancia() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIntoleranciaSeleccionada(new Intolerancia());

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        boolean result = intoleranciaService.actualizarIntoleranciaUsuario(1, null);
        assertThat(result).isTrue();
        assertThat(usuario.getIntoleranciaSeleccionada()).isNull();
    }

    @Test
    void actualizarIntoleranciaUsuario_returnsFalseIfUserNotFound() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        boolean result = intoleranciaService.actualizarIntoleranciaUsuario(99, 1);
        assertThat(result).isFalse();
    }

    @Test
    void actualizarIntoleranciaUsuario_returnsFalseIfIntoleranciaNotFound() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(intoleranciaRepository.findById(100)).thenReturn(Optional.empty());

        boolean result = intoleranciaService.actualizarIntoleranciaUsuario(1, 100);
        assertThat(result).isFalse();
    }
}
