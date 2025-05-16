package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "receta")
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "ready_in_minutes")
    private Integer readyInMinutes;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PasoPreparacion> pasosPreparacion;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Ingrediente> ingredientes;

    // Constructor vacío
    public Receta() {}

    // Constructor con atributos
    public Receta(Integer id, String title, String summary, Integer calories, Integer readyInMinutes, String image, List<PasoPreparacion> pasosPreparacion, List<Ingrediente> ingredientes) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.calories = calories;
        this.readyInMinutes = readyInMinutes;
        this.image = image;
        this.pasosPreparacion = pasosPreparacion;
        this.ingredientes = ingredientes;
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PasoPreparacion> getPasosPreparacion() {
        return pasosPreparacion;
    }

    public void setPasosPreparacion(List<PasoPreparacion> pasosPreparacion) {
        this.pasosPreparacion = pasosPreparacion;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
}
