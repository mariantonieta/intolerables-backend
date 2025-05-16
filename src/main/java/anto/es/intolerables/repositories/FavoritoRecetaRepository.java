package anto.es.intolerables.repositories;

import anto.es.intolerables.entities.FavoritoReceta;
import anto.es.intolerables.entities.Receta;
import anto.es.intolerables.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoRecetaRepository extends JpaRepository<FavoritoReceta, Integer> {
    List<FavoritoReceta> findByUsuario(Usuario usuario);
    boolean existsByUsuarioAndReceta(Usuario usuario, Receta receta);
}
