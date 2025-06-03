package anto.es.intolerables.services;

import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByNombre_devuelveUsuarioCuandoExiste() {
        // Arrange
        String nombre = "antonio";
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);

        when(usuarioRepository.findByNombre(nombre)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = usuarioService.findByNombre(nombre);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo(nombre);
        verify(usuarioRepository, times(1)).findByNombre(nombre);
    }

    @Test
    void findByNombre_devuelveEmptyCuandoNoExiste() {
        String nombre = "inexistente";
        when(usuarioRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findByNombre(nombre);

        assertThat(resultado).isEmpty();
    }

    @Test
    void findById_devuelveUsuarioCuandoExiste() {
        Integer id = 1;
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.findById(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(id);
        verify(usuarioRepository).findById(id);
    }

    @Test
    void findById_devuelveEmptyCuandoNoExiste() {
        Integer id = 99;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findById(id);

        assertThat(resultado).isEmpty();
    }

    @Test
    void save_guardaYDevuelveUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("nuevo");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario guardado = usuarioService.save(usuario);

        assertThat(guardado).isEqualTo(usuario);
        verify(usuarioRepository).save(usuario);
    }
}
