package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int idUsuario;
    private String usuario;
    private String correo;
    private String contrasenia;
    private int idPersona; // ← Campo CRÍTICO que faltaba
    private String token;

    public Usuario() {}

    // Constructor actualizado
    public Usuario(int idUsuario, String usuario, String correo, String contrasenia, int idPersona, String token) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.idPersona = idPersona; // Nuevo campo
        this.token = token;
    }

    // Getters y Setters (actualizados)
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

    // Nuevos getter y setter para idPersona
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() { return getUsuario(); }
}