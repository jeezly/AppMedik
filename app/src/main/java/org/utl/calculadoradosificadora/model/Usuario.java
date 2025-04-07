package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int idUsuario;
    private String usuario;
    private String correo;
    private String contrasenia;
    private String token;

    public Usuario() {}

    public Usuario(int idUsuario, String usuario, String correo, String contrasenia, String token) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.token = token;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}