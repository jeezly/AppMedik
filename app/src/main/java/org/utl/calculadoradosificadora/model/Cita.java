package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Cita implements Serializable {
    private int idCita;
    private String fecha;
    private String hora;
    private String estatus;
    private String razonCita;
    private String nota; // <-- Añade este campo
    private Medico medico;
    private Paciente paciente;
    private Titular titular;

    // Constructor vacío
    public Cita() {}

    // Getters y setters
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

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
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

    // Método para formatear fecha y hora
    public String getFechaHoraFormateada() {
        return fecha + " " + hora;
    }

    // Método para crear una copia mínima necesaria para las operaciones
    public Cita crearCopiaMinima() {
        Cita copia = new Cita();
        copia.setIdCita(this.idCita);
        copia.setFecha(this.fecha);
        copia.setHora(this.hora);
        copia.setEstatus(this.estatus);
        copia.setRazonCita(this.razonCita);
        copia.setNota(this.nota); // <-- Añade esta línea

        // Solo incluir IDs para las relaciones
        if (this.medico != null) {
            Medico m = new Medico();
            m.setIdMedico(this.medico.getIdMedico());
            copia.setMedico(m);
        }

        if (this.paciente != null) {
            Paciente p = new Paciente();
            p.setIdPaciente(this.paciente.getIdPaciente());
            copia.setPaciente(p);
        }

        if (this.titular != null) {
            Titular t = new Titular();
            t.setIdTitular(this.titular.getIdTitular());
            copia.setTitular(t);
        }

        return copia;
    }
}