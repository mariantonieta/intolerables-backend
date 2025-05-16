package anto.es.intolerables.dto;

import anto.es.intolerables.entities.FavoritoReceta;
import java.time.LocalDate;

public class FavoritoRecetaDTO {
    private Integer id;
    private String nombreReceta;
    private LocalDate fecha;

    // Constructor
    public FavoritoRecetaDTO(FavoritoReceta favorito) {
        this.id = favorito.getId();
        this.nombreReceta = favorito.getReceta().getTitle();
        this.fecha = favorito.getFecha();
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
