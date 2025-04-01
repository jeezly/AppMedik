package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Nota implements Serializable {
    private int idNota;
    private String dato;
    private Cita cita;

    public Nota() {}

    public Nota(int idNota, String dato, Cita cita) {
        this.idNota = idNota;
        this.dato = dato;
        this.cita = cita;
    }

    // Getters y Setters
    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "idNota=" + idNota +
                ", dato='" + dato + '\'' +
                ", cita=" + cita +
                '}';
    }
}