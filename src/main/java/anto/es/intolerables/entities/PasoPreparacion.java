package anto.es.intolerables.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "paso_preparacion")
public class PasoPreparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paso")
    private Integer id;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_receta")
    private Receta receta;

    // Constructor vacío
    public PasoPreparacion() {}

    // Constructor con atributos
    public PasoPreparacion(Integer id, String descripcion, Receta receta) {
        this.id = id;
        this.descripcion = descripcion;
        this.receta = receta;
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}
