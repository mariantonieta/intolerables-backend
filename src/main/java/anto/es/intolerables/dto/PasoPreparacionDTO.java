package anto.es.intolerables.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PasoPreparacionDTO {
    private String descripcion;
    public PasoPreparacionDTO() {}

    @JsonCreator
    public PasoPreparacionDTO(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getters y Setters manuales
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
