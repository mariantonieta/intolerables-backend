package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="favorito_restaurante")
//GENERA EL ID ÚNICO YA QUE CUANDO USO YELP API NO ME DA LOS RESTAURANTES CON IDs
public class FavoritoRestaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_favorito_restaurante")
    private Integer id;

    @Column(name="fecha_favorito_restaurante", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference("usuario-favoritos")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_restaurante", nullable = false)
    @JsonBackReference(value = "favorito-restaurante")
    private Restaurante restaurante;

    // Constructor vacío
    public FavoritoRestaurante() {}

    // Constructor con atributos
    public FavoritoRestaurante(Integer id, LocalDate fecha, Usuario usuario, Restaurante restaurante) {
        this.id = id;
        this.fecha = fecha;
        this.usuario = usuario;
        this.restaurante = restaurante;
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
}
