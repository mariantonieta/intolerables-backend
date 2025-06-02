package anto.es.intolerables.services;

import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.repositories.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RestauranteService {
    private final RestauranteRepository repositorio;
    private final GroqService groqService;

    public List<Restaurante> findAll() {
        return repositorio.findAll();
    }

    public Optional<Restaurante> findById(Integer id) {
        return repositorio.findById(id);
    }

    public Restaurante save(Restaurante restaurante) {
        return repositorio.save(restaurante);
    }
    public List<Map<String, String>> buscarRestaurantesConIA(String intolerancia, String comida, String ubicacion) {
        return groqService.buscarRestaurantesIA(intolerancia, comida, ubicacion);
    }
    public List<Restaurante> guardarRestaurantesEnBD(List<Map<String, String>> restaurantesIA) {
        List<Restaurante> restaurantesGuardados = new ArrayList<>();

        for (Map<String, String> mapa : restaurantesIA) {
            Restaurante r = new Restaurante();
            r.setNombre(mapa.get("nombre"));
            r.setDireccion(mapa.get("direccion"));
            r.setUrl(mapa.get("url"));

            // Aquí guardas en la base, el ID se genera automáticamente
            Restaurante guardado = repositorio.save(r);
            restaurantesGuardados.add(guardado);
        }

        return restaurantesGuardados;
    }

}