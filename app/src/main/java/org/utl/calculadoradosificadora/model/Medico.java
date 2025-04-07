package org.utl.calculadoradosificadora.model;

import java.io.Serializable;

public class Medico implements Serializable {
    private int idMedico;
    private String foto;
    private String numCedula;
    private Persona persona;
    private Usuario usuario;

    public Medico() {}

    public Medico(int idMedico, String foto, String numCedula, Persona persona, Usuario usuario) {
        this.idMedico = idMedico;
        this.foto = foto;
        this.numCedula = numCedula;
        this.persona = persona;
        this.usuario = usuario;
    }

    // Getters y Setters
    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNumCedula() {
        return numCedula;
    }

    public void setNumCedula(String numCedula) {
        this.numCedula = numCedula;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Métodos de conveniencia
    public String getNombreCompleto() {
        return persona != null ? "Dr. " + persona.getNombre() + " " + persona.getApellidos() : "Médico no disponible";
    }

    public String getEspecialidad() {
        // Puedes agregar lógica para especialidad si existe en tu modelo
        return "Pediatría"; // Ejemplo estático
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " - Cédula: " + numCedula;
    }
}