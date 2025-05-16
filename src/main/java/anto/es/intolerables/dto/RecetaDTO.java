package anto.es.intolerables.dto;

import java.util.List;

public class RecetaDTO {
    private Integer id;
    private String title;
    private String summary;
    private Integer calories;
    private Integer readyInMinutes;
    private String image;
    private List<PasoPreparacionDTO> pasosPreparacion;
    private List<IngredienteDTO> recetaIngredientes;

    // Constructor vacío
    public RecetaDTO() {}

    // Constructor con atributos
    public RecetaDTO(Integer id, String title, String summary, Integer calories, Integer readyInMinutes, String image, List<PasoPreparacionDTO> pasosPreparacion, List<IngredienteDTO> recetaIngredientes) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.calories = calories;
        this.readyInMinutes = readyInMinutes;
        this.image = image;
        this.pasosPreparacion = pasosPreparacion;
        this.recetaIngredientes = recetaIngredientes;
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

    public List<PasoPreparacionDTO> getPasosPreparacion() {
        return pasosPreparacion;
    }

    public void setPasosPreparacion(List<PasoPreparacionDTO> pasosPreparacion) {
        this.pasosPreparacion = pasosPreparacion;
    }

    public List<IngredienteDTO> getRecetaIngredientes() {
        return recetaIngredientes;
    }

    public void setRecetaIngredientes(List<IngredienteDTO> recetaIngredientes) {
        this.recetaIngredientes = recetaIngredientes;
    }
}
