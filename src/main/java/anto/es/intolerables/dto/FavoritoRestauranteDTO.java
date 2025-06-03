package anto.es.intolerables.dto;

import anto.es.intolerables.entities.FavoritoRestaurante;
import java.time.LocalDate;

public class FavoritoRestauranteDTO {
    private Integer id;
    private String nombreRestaurante;
    private LocalDate fecha;

    // Constructor
    public FavoritoRestauranteDTO(FavoritoRestaurante favorito) { this.id = favorito.getId();
        this.nombreRestaurante = favorito.getRestaurante().getNombre();
        this.fecha = favorito.getFecha();
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Getters y Setters manuales
    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
