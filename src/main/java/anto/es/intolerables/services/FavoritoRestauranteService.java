package anto.es.intolerables.services;
import anto.es.intolerables.entities.FavoritoRestaurante;
import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.entities.Usuario;
import anto.es.intolerables.repositories.FavoritoRestauranteRepository;
import anto.es.intolerables.repositories.RestauranteRepository;
import anto.es.intolerables.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class FavoritoRestauranteService {
    private final FavoritoRestauranteRepository favoritoRepo;
    private final UsuarioRepository usuarioRepo;
    private final RestauranteRepository restauranteRepo;
    public List<FavoritoRestaurante> findAll() {
        return favoritoRepo.findAll();
    }
    public Optional<FavoritoRestaurante> findByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante) {
        return favoritoRepo.findByUsuarioAndRestaurante(usuario, restaurante);
    }
    public void agregarFavorito(Integer usuarioId, Integer restauranteId) {
        if (favoritoRepo.existsByUsuarioIdAndRestauranteId(usuarioId, restauranteId)) {
            throw new RuntimeException("El restaurante ya está en favoritos");
        }

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Restaurante restaurante = restauranteRepo.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        FavoritoRestaurante favorito = new FavoritoRestaurante();
        favorito.setUsuario(usuario);
        favorito.setRestaurante(restaurante);
        favorito.setFecha(LocalDate.now());

        favoritoRepo.save(favorito);
    }

    public Optional<FavoritoRestaurante> findById(Integer id) {
        return favoritoRepo.findById(id);
    }

    public FavoritoRestaurante save(FavoritoRestaurante favorito) {
        return favoritoRepo.save(favorito);
    }

    public void deleteById(Integer id) {
        favoritoRepo.deleteById(id);
    }
}