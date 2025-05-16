package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
Optional<Receta> findByTitle(String title);
}
