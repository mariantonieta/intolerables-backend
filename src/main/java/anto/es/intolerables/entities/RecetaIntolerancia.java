package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "receta_intolerancia")
public class RecetaIntolerancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta_intolerancia")
    private Integer id;

    @Column(name = "cantidad_intolerancia")
    private String cantidadIntolerancia;

    @ManyToOne
    @JoinColumn(name = "id_receta")
    @JsonBackReference("receta-intolerancias")
    private Receta receta;

    @ManyToOne
    @JoinColumn(name = "id_intolerancia")
    private Intolerancia intolerancia;

    // Constructor vacío
    public RecetaIntolerancia() {}

    // Constructor con atributos
    public RecetaIntolerancia(Integer id, String cantidadIntolerancia, Receta receta, Intolerancia intolerancia) {
        this.id = id;
        this.cantidadIntolerancia = cantidadIntolerancia;
        this.receta = receta;
        this.intolerancia = intolerancia;
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCantidadIntolerancia() {
        return cantidadIntolerancia;
    }

    public void setCantidadIntolerancia(String cantidadIntolerancia) {
        this.cantidadIntolerancia = cantidadIntolerancia;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Intolerancia getIntolerancia() {
        return intolerancia;
    }

    public void setIntolerancia(Intolerancia intolerancia) {
        this.intolerancia = intolerancia;
    }
}
