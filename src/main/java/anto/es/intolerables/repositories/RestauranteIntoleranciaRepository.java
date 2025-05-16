package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.RestauranteIntolerancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RestauranteIntoleranciaRepository extends JpaRepository<RestauranteIntolerancia, Integer> {
}
