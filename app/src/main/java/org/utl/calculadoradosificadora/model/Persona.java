package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Persona implements Serializable {
    private int idPersona;
    private String nombre;
    private String apellidos;
    private int genero; // 1: masculino, 0: femenino
    private int estado; // 1: activo, 0: inactivo

    public Persona() {}

    public Persona(int idPersona, String nombre, String apellidos, int genero, int estado) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.genero = genero;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre != null ? nombre : "";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos != null ? apellidos : "";
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getGeneroTexto() {
        return genero == 1 ? "Masculino" : "Femenino";
    }

    @Override
    public String toString() {
        return getNombre() + " " + getApellidos();
    }
}