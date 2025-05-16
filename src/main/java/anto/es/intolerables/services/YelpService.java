package anto.es.intolerables.services;

import anto.es.intolerables.dto.YelpDto;
import anto.es.intolerables.entities.Restaurante;
import anto.es.intolerables.repositories.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class YelpService {

    private final RestauranteRepository restauranteRepository;

    private static final String YELP_API_URL = "https://api.yelp.com/v3/businesses/search";
    private final RestTemplate restTemplate;
    //apiKey que esta en applications.properties
    @Value("${yelp.api.key}")
    private String yelpApiKey;

//para poder realizar el test
    public void setYelpApiKey(String yelpApiKey) {
        this.yelpApiKey = yelpApiKey;
    }

    //busca los restaurantes por los parametros
    //intolerancia (el mas importante)
    //ubicacion
    //comida
    public List<Restaurante> buscarPorIntolerancia(String intolerancia, String ubicacion, String comida) {
        String categoriaYelp = getYelpCategoryForIntolerancia(intolerancia);
        return buscarRestaurantes(categoriaYelp, ubicacion, comida);
    }

    public List<Restaurante> buscarRestaurantes(String termino, String ubicacion, String comida) {
        // la url para que la busqueda sea mas personalizada
        String term = termino + " " + (comida != null ? comida : "");
        String url = String.format("%s?term=%s&location=%s", YELP_API_URL, term.trim(), ubicacion);

        //autorizacion con la apiKey
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + yelpApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        //llama a la api
        ResponseEntity<YelpDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, YelpDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<YelpDto.Business> businesses = response.getBody().getBusinesses();
//transformar los datos a JSON
            return businesses.stream()
                    .map(business -> {
                        Restaurante restaurante = new Restaurante();
                        restaurante.setNombre(business.getName());
                        restaurante.setDireccion(business.getLocation().getAddress1());
                        restaurante.setCategoria(business.getCategories().get(0).getTitle());
                        restaurante.setLatitud(business.getCoordinates().getLatitude());
                        restaurante.setLongitud(business.getCoordinates().getLongitude());
                        restaurante.setImagen(business.getImageUrl());
                        restaurante.setUrl(business.getUrl());
                        return restaurante;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Error al consultar la API de Yelp");
        }
    }

    // mapea la intolerancia a la categoría correspondiente de Yelp
    private String getYelpCategoryForIntolerancia(String intolerancia) {
        switch (intolerancia.toLowerCase()) {
            case "gluten":
            case "sin gluten":
                return "gluten_free";
            case "vegano":
                return "vegan";
            case "vegetariano":
                return "vegetarian";
            case "lactosa":
            case "sin lactosa":
                return "dairy-free";
            case "frutos secos":
            case "sin frutos secos":
                return "nut-free";
            case "huevo":
            case "sin huevo":
                return "egg-free";
            case "soja":
            case "sin soja":
                return "soy-free";
            default:
                return "healthy";  }
    }
}
