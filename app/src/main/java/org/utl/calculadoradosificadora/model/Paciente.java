package org.utl.calculadoradosificadora.model;

public class Paciente extends Persona {
    private int idPaciente;
    private String fechaNacimiento;
    private double peso;
    private String seguro;

    // Constructor vacío
    public Paciente() {
    }

    // Constructor con parámetros
    public Paciente(int idPersona, String nombre, String apellidos, boolean genero, int idPaciente, String fechaNacimiento, double peso, String seguro) {
        super(idPersona, nombre, apellidos, genero); // Corrección aquí
        this.idPaciente = idPaciente;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.seguro = seguro;
    }

    // Getters y Setters
    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getSeguro() {
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "idPaciente=" + idPaciente +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", peso=" + peso +
                ", seguro='" + seguro + '\'' +
                "} " + super.toString();
    }
}
