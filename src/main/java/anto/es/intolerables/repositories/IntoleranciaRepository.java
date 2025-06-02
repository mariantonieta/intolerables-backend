package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.Intolerancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IntoleranciaRepository extends JpaRepository<Intolerancia, Integer> {
    Optional<Intolerancia> findByNombreContainingIgnoreCase(String nombre);
    Intolerancia findByNombre(String nombre);
    List<Intolerancia> findByIdIn(List<Integer> ids);

 }
