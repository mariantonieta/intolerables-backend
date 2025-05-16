package anto.es.intolerables.dto;

import anto.es.intolerables.contraints.PasswordIgual;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@PasswordIgual
public class UsuarioDTO {
    @NotNull
    @NotEmpty
    private String nombre;
    private String contrasena;

    @NotNull
    @NotEmpty
    private String contrasenaConfirm;
    private String paisUsuario;
    private String ciudad;

    // Constructor vacío
    public UsuarioDTO() {}

    // Constructor con atributos
    public UsuarioDTO(String nombre, String contrasena, String contrasenaConfirm, String paisUsuario, String ciudad) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.contrasenaConfirm = contrasenaConfirm;
        this.paisUsuario = paisUsuario;
        this.ciudad = ciudad;
    }

    // Getters y Setters manuales
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

    public String getContrasenaConfirm() {
        return contrasenaConfirm;
    }

    public void setContrasenaConfirm(String contrasenaConfirm) {
        this.contrasenaConfirm = contrasenaConfirm;
    }

    public String getPaisUsuario() {
        return paisUsuario;
    }

    public void setPaisUsuario(String paisUsuario) {
        this.paisUsuario = paisUsuario;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
