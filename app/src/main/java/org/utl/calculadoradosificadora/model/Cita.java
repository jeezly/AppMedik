package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Cita implements Serializable {
    private int idCita;
    private String fecha;  // Formato: "YYYY-MM-DD"
    private String hora;
    private String estatus;  // Ej: "Programada", "Cancelada", "Atendida"
    private String razonCita;  // Nombre exacto como en el backend
    private Medico medico;     // Requerido
    private Paciente paciente; // Requerido
    private Titular titular;   // Requerido

    // Constructor vacío
    public Cita() {}

    // Getters y Setters (¡Todos deben existir!)
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

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getRazonCita() {
        return razonCita;
    }

    public void setRazonCita(String razonCita) {
        this.razonCita = razonCita;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Titular getTitular() {
        return titular;
    }

    public void setTitular(Titular titular) {
        this.titular = titular;
    }
}