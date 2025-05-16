package anto.es.intolerables.dto;

public class IngredienteDTO {
    private Integer id;
    private String nombre;
    private String cantidad;

    // Constructor con todos los atributos
    public IngredienteDTO(Integer id, String nombre, String cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
