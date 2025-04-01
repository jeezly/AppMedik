package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private int idMedicamentos;
    private String presentacion;
    private String nombre;
    private String viaAdministracion;
    private String descripcion;
    private double dosisPorKg; // mg/kg
    private double concentracion; // mg/ml

    public Medicamento() {}

    public Medicamento(int idMedicamentos, String presentacion, String nombre,
                       String viaAdministracion, String descripcion,
                       double dosisPorKg, double concentracion) {
        this.idMedicamentos = idMedicamentos;
        this.presentacion = presentacion;
        this.nombre = nombre;
        this.viaAdministracion = viaAdministracion;
        this.descripcion = descripcion;
        this.dosisPorKg = dosisPorKg;
        this.concentracion = concentracion;
    }

    // Getters y Setters
    public int getIdMedicamentos() {
        return idMedicamentos;
    }

    public void setIdMedicamentos(int idMedicamentos) {
        this.idMedicamentos = idMedicamentos;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getDosisPorKg() {
        return dosisPorKg;
    }

    public void setDosisPorKg(double dosisPorKg) {
        this.dosisPorKg = dosisPorKg;
    }

    public double getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(double concentracion) {
        this.concentracion = concentracion;
    }

    @Override
    public String toString() {
        return nombre + " (" + presentacion + ")";
    }
}