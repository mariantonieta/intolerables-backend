package anto.es.intolerables.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "restaurante_intolerancia")
public class RestauranteIntolerancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restaurante_intolerancia")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_restaurante")
    @JsonBackReference("restaurante-intolerancias")
    private Restaurante restaurante;

    @ManyToOne
    @JoinColumn(name = "id_intolerancia")
    @JsonBackReference("intolerancia-restaurantes")
    private Intolerancia intolerancia;

    // Constructor vacío
    public RestauranteIntolerancia() {}

    // Constructor con atributos
    public RestauranteIntolerancia(Integer id, Restaurante restaurante, Intolerancia intolerancia) {
        this.id = id;
        this.restaurante = restaurante;
        this.intolerancia = intolerancia;
    }

    // Getters y Setters manuales
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Intolerancia getIntolerancia() {
        return intolerancia;
    }

    public void setIntolerancia(Intolerancia intolerancia) {
        this.intolerancia = intolerancia;
    }
}
