package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.Intolerancia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // No reemplazar datasource, usar el configurado (Postgres)

      @ActiveProfiles("test")
    public class IntoleranciaRepositoryTest {

        @Autowired
        private IntoleranciaRepository intoleranciaRepository;

        @BeforeEach
        public void setUp() {
            intoleranciaRepository.deleteAll();
        }

        @Test
        @DisplayName("Guardar y buscar Intolerancia por nombre exacto")
        public void testFindByNombre() {
            Intolerancia i = new Intolerancia();
            i.setNombre("Lactosa");
            intoleranciaRepository.save(i);

            Intolerancia found = intoleranciaRepository.findByNombre("Lactosa");

            assertThat(found).isNotNull();
            assertThat(found.getNombre()).isEqualTo("Lactosa");
        }


    }
