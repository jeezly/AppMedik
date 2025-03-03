package org.utl.calculadoradosificadora.model;

public class Medico extends Persona {
    private int idMedico;
    private String foto;
    private int numCedula; // Ahora es int

    public Medico() {}

    public Medico(int idPersona, String nombre, String apellidos, boolean genero, int idMedico, String foto, int numCedula) {
        super(idPersona, nombre, apellidos, genero);
        this.idMedico = idMedico;
        this.foto = foto;
        this.numCedula = numCedula;
    }

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

    public int getNumCedula() {
        return numCedula;
    }

    public void setNumCedula(int numCedula) {
        this.numCedula = numCedula;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "idMedico=" + idMedico +
                ", foto='" + foto + '\'' +
                ", numCedula=" + numCedula +
                "} " + super.toString();
    }
}
