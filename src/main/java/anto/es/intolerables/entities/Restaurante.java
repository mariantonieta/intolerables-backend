package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name= "restaurante")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_restaurante")
    private Integer id;

    @Column(name = "nombre_restaurante")
    private String nombre;

    @Column(name = "direccion_restaurante")
    private String direccion;

    @Column(name = "categoria_restaurante")
    private String categoria;

    private Double latitud;
    private Double longitud;
    private String imagen;
    private String url;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "restaurante-intolerancias")
    private List<RestauranteIntolerancia> intolerancias;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "favorito-restaurante")
    private List<FavoritoRestaurante> favoritos;

    // Constructor vacío
    public Restaurante() {}

    // Constructor con atributos
    public Restaurante(Integer id, String nombre, String direccion, String categoria, Double latitud, Double longitud, String imagen, String url, List<RestauranteIntolerancia> intolerancias, List<FavoritoRestaurante> favoritos) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
        this.url = url;
        this.intolerancias = intolerancias;
        this.favoritos = favoritos;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<RestauranteIntolerancia> getIntolerancias() {
        return intolerancias;
    }

    public void setIntolerancias(List<RestauranteIntolerancia> intolerancias) {
        this.intolerancias = intolerancias;
    }

    public List<FavoritoRestaurante> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<FavoritoRestaurante> favoritos) {
        this.favoritos = favoritos;
    }
}
