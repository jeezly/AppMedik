package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Titular implements Serializable {
    private int idTitular;
    private String telefono;
    private Persona persona;
    private Usuario usuario;

    public Titular() {}

    public Titular(int idTitular, String telefono, Persona persona, Usuario usuario) {
        this.idTitular = idTitular;
        this.telefono = telefono;
        this.persona = persona;
        this.usuario = usuario;
    }

    // Getters y Setters
    public int getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(int idTitular) {
        this.idTitular = idTitular;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Métodos de conveniencia para acceder a datos de Persona
    public String getNombre() {
        return persona != null ? persona.getNombre() : "";
    }

    public String getApellidos() {
        return persona != null ? persona.getApellidos() : "";
    }

    public String getGenero() {
        return persona != null ? persona.getGeneroTexto() : "";
    }

    public String getCorreo() {
        return usuario != null ? usuario.getCorreo() : "";
    }

    public String getUsuarioNombre() {
        return usuario != null ? usuario.getUsuario() : "";
    }

    @Override
    public String toString() {
        return getNombre() + " " + getApellidos();
    }
}