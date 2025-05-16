package anto.es.intolerables.entities;

import jakarta.persistence.*;

@Entity
@Table(name="ingrediente")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingrediente")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad", nullable = false)
    private String cantidad;

    @ManyToOne
    @JoinColumn(name = "id_receta")
    private Receta receta;

    // Constructor vacío
    public Ingrediente() {}

    // Constructor con atributos
    public Ingrediente(Integer id, String nombre, String cantidad, Receta receta) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.receta = receta;
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

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}
