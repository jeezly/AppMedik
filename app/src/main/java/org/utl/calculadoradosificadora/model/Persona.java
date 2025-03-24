package org.utl.calculadoradosificadora.model;
import java.io.Serializable;

public class Persona implements Serializable {
    private int idPersona;
    private String nombre;
    private String apellidos;
    private boolean genero;

    // Constructor vacío
    public Persona() {
    }

    // Constructor con parámetros
    public Persona(int idPersona, String nombre, String apellidos, boolean genero) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.genero = genero;
    }

    // Getters y Setters
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
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

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "idPersona=" + idPersona +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", genero=" + genero +
                '}';
    }
}
