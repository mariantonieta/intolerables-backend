package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "nombre_usuario")
    private String nombre;

    @Column(name = "contraseña_usuario")
    private String contrasena;

    @Column(name = "fecha_registro_usuario")
    private LocalDate fechaRegistro;

    @Column(name = "pais_usuario")
    private String paisUsuario;

    @Column(name = "ciudad_usuario")
    private String ciudadUsuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "usuario-favoritos")
    private List<FavoritoRestaurante> favoritos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "usuario-favoritos-recetas")
    private List<FavoritoReceta> favorito;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "usuario-intolerancias")
    private List<UsuarioIntolerancia> intolerancias;

    // Constructor vacío
    public Usuario() {}

    // Constructor con atributos
    public Usuario(Integer id, String nombre, String contrasena, LocalDate fechaRegistro, String paisUsuario, String ciudadUsuario, List<FavoritoRestaurante> favoritos, List<FavoritoReceta> favorito, List<UsuarioIntolerancia> intolerancias) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.fechaRegistro = fechaRegistro;
        this.paisUsuario = paisUsuario;
        this.ciudadUsuario = ciudadUsuario;
        this.favoritos = favoritos;
        this.favorito = favorito;
        this.intolerancias = intolerancias;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getPaisUsuario() {
        return paisUsuario;
    }

    public void setPaisUsuario(String paisUsuario) {
        this.paisUsuario = paisUsuario;
    }

    public String getCiudadUsuario() {
        return ciudadUsuario;
    }

    public void setCiudadUsuario(String ciudadUsuario) {
        this.ciudadUsuario = ciudadUsuario;
    }

    public List<FavoritoRestaurante> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<FavoritoRestaurante> favoritos) {
        this.favoritos = favoritos;
    }

    public List<FavoritoReceta> getFavorito() {
        return favorito;
    }

    public void setFavorito(List<FavoritoReceta> favorito) {
        this.favorito = favorito;
    }

    public List<UsuarioIntolerancia> getIntolerancias() {
        return intolerancias;
    }

    public void setIntolerancias(List<UsuarioIntolerancia> intolerancias) {
        this.intolerancias = intolerancias;
    }
}
