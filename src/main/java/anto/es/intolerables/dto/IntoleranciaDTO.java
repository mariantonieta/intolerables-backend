package anto.es.intolerables.dto;

import anto.es.intolerables.entities.Intolerancia;

public class IntoleranciaDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private String detalles;
    private String mensaje;

    // Constructor con todos los atributos
    public IntoleranciaDTO(Integer id, String nombre, String descripcion, String imagen, String detalles, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.detalles = detalles;
        this.mensaje = mensaje;
    }

    // Constructor con una entidad Intolerancia
    public IntoleranciaDTO(Intolerancia intolerancia) {
        this.id = intolerancia.getId();
        this.nombre = intolerancia.getNombre();
        this.descripcion = intolerancia.getDescripcion();
        this.imagen = intolerancia.getImagen();
        this.detalles = intolerancia.getDetalles();
        this.mensaje = intolerancia.getMensaje();
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
