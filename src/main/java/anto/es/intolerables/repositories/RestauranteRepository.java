package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {
    Optional<Restaurante> findByNombre(String nombre);
    Optional<Restaurante> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombreAndDireccion(String nombre, String direccion);

}
