package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Titular implements Serializable {
    private int idTitular;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String usuario;
    private String genero;

    public Titular() {}

    public Titular(int idTitular, String nombre, String apellidos, String correo, String telefono, String usuario, String genero) {
        this.idTitular = idTitular;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.usuario = usuario;
        this.genero = genero;
    }

    // Getters y Setters
    public int getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(int idTitular) {
        this.idTitular = idTitular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Titular{" +
                "idTitular=" + idTitular +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", usuario='" + usuario + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}