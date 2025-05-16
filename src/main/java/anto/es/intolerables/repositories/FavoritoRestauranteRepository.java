package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.FavoritoRestaurante;
import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRestauranteRepository extends JpaRepository<FavoritoRestaurante, Integer> {
    List<FavoritoRestaurante> findByUsuarioId(Integer usuarioId);
    boolean existsByUsuarioIdAndRestauranteId(Integer usuarioId, Integer restauranteId);
    Optional<FavoritoRestaurante> findByUsuarioIdAndRestauranteId(Integer usuarioId, Integer restauranteId);
    Optional<FavoritoRestaurante> findByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante);
    List<FavoritoRestaurante> findByUsuario(Usuario usuario);
}
