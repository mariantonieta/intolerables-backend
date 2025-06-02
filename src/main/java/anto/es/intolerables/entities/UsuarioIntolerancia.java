package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario_intolerancia")
public class UsuarioIntolerancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_intolerancia")
    private Integer id;

    @Column(name = "numero_intolerancia")
    private String numeroIntolerancia;

    @ManyToOne
    @JoinColumn(name= "id_usuario")
    @JsonBackReference("usuario-intolerancias")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name= "id_intolerancia")
    @JsonBackReference("intolerancia-usuarios")
    private Intolerancia intolerancia;

    // Constructor vacío
    public UsuarioIntolerancia() {}
    public UsuarioIntolerancia(Usuario usuario, Intolerancia intolerancia) {
        this.usuario = usuario;
        this.intolerancia = intolerancia;
    }
    // Constructor con atributos
    public UsuarioIntolerancia(Integer id, String numeroIntolerancia, Usuario usuario, Intolerancia intolerancia) {
        this.id = id;
        this.numeroIntolerancia = numeroIntolerancia;
        this.usuario = usuario;
        this.intolerancia = intolerancia;
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroIntolerancia() {
        return numeroIntolerancia;
    }

    public void setNumeroIntolerancia(String numeroIntolerancia) {
        this.numeroIntolerancia = numeroIntolerancia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Intolerancia getIntolerancia() {
        return intolerancia;
    }

    public void setIntolerancia(Intolerancia intolerancia) {
        this.intolerancia = intolerancia;
    }
}
