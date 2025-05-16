package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "intolerancia")
public class Intolerancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_intolerancia")
    private Integer id;

    @Column(name="nombre_intolerancia")
    private String nombre;

    @Column(name="descripcion_intolerancia")
    private String descripcion;

    @Column(length = 1200)
    private String detalles;

    private String mensaje;
    private String imagen;

    @OneToMany(mappedBy = "intolerancia")
    @JsonBackReference("receta-intolerancias")
    private List<RecetaIntolerancia> recetas;

    @OneToMany(mappedBy = "intolerancia", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "intolerancia-usuarios")
    private List<UsuarioIntolerancia> usuarios;

    @OneToMany(mappedBy = "intolerancia", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "intolerancia-restaurantes")
    private List<RestauranteIntolerancia> restaurantes;

    // Constructor vacío
    public Intolerancia() {}

    // Constructor con atributos
    public Intolerancia(Integer id, String nombre, String descripcion, String detalles, String mensaje, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.detalles = detalles;
        this.mensaje = mensaje;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<RecetaIntolerancia> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<RecetaIntolerancia> recetas) {
        this.recetas = recetas;
    }

    public List<UsuarioIntolerancia> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioIntolerancia> usuarios) {
        this.usuarios = usuarios;
    }

    public List<RestauranteIntolerancia> getRestaurantes() {
        return restaurantes;
    }

    public void setRestaurantes(List<RestauranteIntolerancia> restaurantes) {
        this.restaurantes = restaurantes;
    }
}
