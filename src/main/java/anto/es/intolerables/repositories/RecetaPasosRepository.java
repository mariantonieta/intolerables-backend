package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.PasoPreparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaPasosRepository extends JpaRepository<PasoPreparacion, Integer> {
}
