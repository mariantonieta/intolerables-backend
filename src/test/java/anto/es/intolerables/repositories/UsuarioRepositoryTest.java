package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Guardar y encontrar usuario por nombre")
    void testFindByNombre() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setContrasena("1234");
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setPaisUsuario("España");
        usuario.setCiudadUsuario("Madrid");

        usuarioRepository.save(usuario);

        // When
        Optional<Usuario> encontrado = usuarioRepository.findByNombre("Juan");

        // Then
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Juan");
        assertThat(encontrado.get().getCiudadUsuario()).isEqualTo("Madrid");
    }

    @Test
    @DisplayName("Buscar usuario que no existe")
    void testUsuarioNoEncontrado() {
        Optional<Usuario> resultado = usuarioRepository.findByNombre("NoExiste");
        assertThat(resultado).isNotPresent();
    }
}
