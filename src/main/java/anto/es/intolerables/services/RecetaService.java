package anto.es.intolerables.services;

import anto.es.intolerables.dto.*;
import anto.es.intolerables.entities.*;
import anto.es.intolerables.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final TraduccionService traduccionService;

    public RecetaService(RecetaRepository recetaRepository, IngredienteRepository ingredienteRepository, TraduccionService traduccionService) {
        this.recetaRepository = recetaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.traduccionService = traduccionService;
    }

    @Transactional
    public RecetaDTO crearReceta(RecetaDTO recetaDTO) {
        Receta receta = new Receta();
        receta.setTitle(recetaDTO.getTitle());
        receta.setSummary(recetaDTO.getSummary());
        receta.setCalories(recetaDTO.getCalories());
        receta.setReadyInMinutes(recetaDTO.getReadyInMinutes());
        receta.setImage(recetaDTO.getImage());

        List<PasoPreparacion> pasos = recetaDTO.getPasosPreparacion() != null ? recetaDTO.getPasosPreparacion().stream()
                .map(dto -> {
                    PasoPreparacion paso = new PasoPreparacion();
                    paso.setDescripcion(dto.getDescripcion());
                    paso.setReceta(receta);
                    return paso;
                })
                .collect(Collectors.toList()) : new ArrayList<>();

        receta.setPasosPreparacion(pasos);

        List<Ingrediente> ingredientes = recetaDTO.getRecetaIngredientes() != null ? recetaDTO.getRecetaIngredientes().stream()
                .map(dto -> {
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente.setNombre(dto.getNombre());
                    ingrediente.setCantidad(dto.getCantidad());
                    ingrediente.setReceta(receta);

                    return ingrediente;
                })
                .collect(Collectors.toList()) : new ArrayList<>();

        receta.setIngredientes(ingredientes);

        Receta recetaGuardada = recetaRepository.save(receta);

        return convertirADTO(recetaGuardada);
    }
    @Transactional
    public List<RecetaDTO> obtenerTodasLasRecetas() {
        return recetaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    private RecetaDTO convertirADTO(Receta receta) {

        List<PasoPreparacionDTO> pasos = receta.getPasosPreparacion() != null
                ? receta.getPasosPreparacion().stream()
                .map(p -> new PasoPreparacionDTO(p.getDescripcion()))
                .collect(Collectors.toList())
                : new ArrayList<>();

        List<IngredienteDTO> ingredientes = receta.getIngredientes() != null
                ? receta.getIngredientes().stream()
                .map(i -> new IngredienteDTO(i.getId(), i.getNombre(), i.getCantidad()))
                .collect(Collectors.toList())
                : new ArrayList<>();

        RecetaDTO dto = new RecetaDTO();
        dto.setId(receta.getId());
        dto.setTitle(receta.getTitle());
        dto.setSummary(receta.getSummary());
        dto.setCalories(receta.getCalories());
        dto.setReadyInMinutes(receta.getReadyInMinutes());
        dto.setImage(receta.getImage());
        dto.setPasosPreparacion(pasos);
        dto.setRecetaIngredientes(ingredientes);
        return dto;
    }

    public Receta convertirDesdeSpoonacular(Map<String, Object> datosReceta) {
        Receta receta = new Receta();
        receta.setTitle((String) datosReceta.get("title"));
        receta.setImage((String) datosReceta.get("image"));
        receta.setReadyInMinutes((Integer) datosReceta.get("readyInMinutes"));
        receta.setCalories((Integer) datosReceta.getOrDefault("calories", 0));

        List<Map<String, Object>> ingredientesApi = (List<Map<String, Object>>) datosReceta.get("extendedIngredients");
        List<Ingrediente> ingredientes = ingredientesApi.stream()
                .map(ingredienteMap -> {
                    String nombreIngrediente = (String) ingredienteMap.get("name");
                    Object rawCantidad = ingredienteMap.get("amount");
                    String unidad = (String) ingredienteMap.get("unit");
                    String cantidadStr;

                    if (rawCantidad instanceof Integer) {
                        cantidadStr = ((Integer) rawCantidad) + " " + unidad;
                    } else if (rawCantidad instanceof Double) {
                        cantidadStr = String.format("%.2f %s", (Double) rawCantidad, unidad);
                    } else {
                        cantidadStr = "Cantidad desconocida";
                    }
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente.setNombre(nombreIngrediente);
                    ingrediente.setCantidad(cantidadStr.trim());
                    ingrediente.setReceta(receta);
                    return ingrediente;
                })
                .collect(Collectors.toList());
        receta.setIngredientes(ingredientes);


        List<Map<String, Object>> instruccionesApi = (List<Map<String, Object>>) datosReceta.get("analyzedInstructions");


        List<PasoPreparacion> pasos = new ArrayList<>();
        if (instruccionesApi != null) {
            pasos = instruccionesApi.stream()
                    .flatMap(instruccion -> {
                        List<Map<String, Object>> steps = (List<Map<String, Object>>) instruccion.get("steps");


                        if (steps != null) {
                            return steps.stream().map(pasoMap -> {
                                PasoPreparacion paso = new PasoPreparacion();
                                paso.setDescripcion((String) pasoMap.get("step"));
                                paso.setReceta(receta);
                                return paso;
                            });
                        } else {
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.toList());
        }

        receta.setPasosPreparacion(pasos);

        return receta;
    }
    public RecetaDTO traducirRecetaDTO(RecetaDTO recetaDTO, String idiomaDestino) {
        // Establecer idioma de origen como español ('es') porque la receta está en español en la base de datos
        String idiomaOrigen = "es"; // El idioma de origen es español

        // Traducir los campos de la receta (título, resumen)
        recetaDTO.setTitle(traduccionService.traducirTextoLibreTranslate(recetaDTO.getTitle(), idiomaOrigen, idiomaDestino));
        recetaDTO.setSummary(traduccionService.traducirTextoLibreTranslate(recetaDTO.getSummary(), idiomaOrigen, idiomaDestino));

        // Traducir ingredientes
        for (IngredienteDTO ingredienteDTO : recetaDTO.getRecetaIngredientes()) {
            ingredienteDTO.setCantidad(traduccionService.traducirTextoLibreTranslate(ingredienteDTO.getCantidad(), idiomaOrigen, idiomaDestino));
            ingredienteDTO.setNombre(traduccionService.traducirTextoLibreTranslate(ingredienteDTO.getNombre(), idiomaOrigen, idiomaDestino));
        }

        // Traducir pasos de preparación
        for (PasoPreparacionDTO pasoDTO : recetaDTO.getPasosPreparacion()) {
            pasoDTO.setDescripcion(traduccionService.traducirTextoLibreTranslate(pasoDTO.getDescripcion(), idiomaOrigen, idiomaDestino));
        }

        return recetaDTO;
    }



}
