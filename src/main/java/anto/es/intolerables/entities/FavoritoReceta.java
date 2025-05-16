package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="favorito_receta")
public class FavoritoReceta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_favorito_receta")
    private Integer id;

    @Column(name="fecha_favorito")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonBackReference(value = "usuario-favoritos-recetas") // Debe coincidir con el de Usuario
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_receta")
    private Receta receta;

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

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}
