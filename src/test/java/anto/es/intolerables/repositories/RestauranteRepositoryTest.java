package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.entities.RestauranteIntolerancia;
import anto.es.intolerables.entities.FavoritoRestaurante;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RestauranteRepositoryTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @BeforeEach
    void setUp() {
        // Insertar datos de prueba con todos los parámetros necesarios
        Restaurante restaurante1 = new Restaurante(
                null,                      // ID (null porque se autogenera)
                "La Casa",                 // Nombre
                "Madrid",                  // Dirección
                "Española",                // Categoría
                40.4168,                    // Latitud
                -3.7038,                    // Longitud
                "https://example.com/casa.jpg", // Imagen
                "https://example.com",     // URL
                List.of(),                  // Lista de intolerancias (vacía en este caso)
                List.of()                   // Lista de favoritos (vacía en este caso)
        );

        Restaurante restaurante2 = new Restaurante(
                null,
                "El Rincón",
                "Barcelona",
                "Mediterránea",
                41.3825,
                2.1769,
                "https://example.com/rincon.jpg",
                "https://example.com",
                List.of(),
                List.of()
        );

        restauranteRepository.saveAll(List.of(restaurante1, restaurante2));
    }

    @AfterEach
    void tearDown() {
        // Limpiar datos después de cada prueba
        restauranteRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe encontrar un restaurante por nombre correctamente")
    void testFindByNombre() {
        Optional<Restaurante> resultado = restauranteRepository.findByNombre("La Casa");
        assertTrue(resultado.isPresent());
        assertEquals("La Casa", resultado.get().getNombre());
    }

    @Test
    @DisplayName("Debe encontrar un restaurante con búsqueda insensible a mayúsculas")
    void testFindByNombreContainingIgnoreCase() {
        Optional<Restaurante> resultado = restauranteRepository.findByNombreContainingIgnoreCase("cAsA");
        assertTrue(resultado.isPresent());
        assertEquals("La Casa", resultado.get().getNombre());
    }

    @Test
    @DisplayName("Debe validar existencia por nombre y dirección")
    void testExistsByNombreAndDireccion() {
        boolean existe = restauranteRepository.existsByNombreAndDireccion("La Casa", "Madrid");
        assertTrue(existe);

        boolean noExiste = restauranteRepository.existsByNombreAndDireccion("La Casa", "Barcelona");
        assertFalse(noExiste);
    }
}
