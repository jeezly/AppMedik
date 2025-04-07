package org.utl.calculadoradosificadora.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Paciente implements Serializable {
    private int idPaciente;
    private String fechaNacimiento;
    private double peso;
    private String seguro;
    private Persona persona;

    public Paciente() {}

    public Paciente(int idPaciente, String fechaNacimiento, double peso, String seguro, Persona persona) {
        this.idPaciente = idPaciente;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.seguro = seguro;
        this.persona = persona;
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    // Métodos de conveniencia
    public String getNombre() {
        return persona != null ? persona.getNombre() : "";
    }

    public String getApellidos() {
        return persona != null ? persona.getApellidos() : "";
    }

    public String getNombreCompleto() {
        return getNombre() + " " + getApellidos();
    }

    public int getEdad() {
        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            return 0;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date fechaNac = sdf.parse(fechaNacimiento);
            Calendar dob = Calendar.getInstance();
            dob.setTime(fechaNac);
            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}