package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Cita implements Serializable { // Implementa Serializable
    private int idCita;
    private String fecha;
    private String hora;
    private String razon;
    private String estatus;
    private Paciente paciente;

    public Cita() {}

    public Cita(int idCita, String fecha, String hora, String razon, String estatus, Paciente paciente) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.razon = razon;
        this.estatus = estatus;
        this.paciente = paciente;
    }

    // Getters y Setters
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", razon='" + razon + '\'' +
                ", estatus='" + estatus + '\'' +
                ", paciente=" + paciente +
                '}';
    }
}