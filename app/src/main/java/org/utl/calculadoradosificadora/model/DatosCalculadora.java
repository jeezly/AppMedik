package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class DatosCalculadora implements Serializable {
    private int idDatosCalculadora;
    private Paciente paciente;
    private Medicamento medicamento;

    public DatosCalculadora() {}

    public DatosCalculadora(int idDatosCalculadora, Paciente paciente, Medicamento medicamento) {
        this.idDatosCalculadora = idDatosCalculadora;
        this.paciente = paciente;
        this.medicamento = medicamento;
    }

    // Getters y Setters
    public int getIdDatosCalculadora() {
        return idDatosCalculadora;
    }

    public void setIdDatosCalculadora(int idDatosCalculadora) {
        this.idDatosCalculadora = idDatosCalculadora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public String toString() {
        return "DatosCalculadora{" +
                "idDatosCalculadora=" + idDatosCalculadora +
                ", paciente=" + paciente +
                ", medicamento=" + medicamento +
                '}';
    }
}