package org.utl.calculadoradosificadora.model;

public class Usuario extends Persona {
    private int idUsuario;
    private String contrasenia;
    private String correo;
    private String nombreUsuario;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros (corrigiendo el tipo de 'genero' de String a boolean)
    public Usuario(int idPersona, String nombre, String apellidos, boolean genero, int idUsuario, String contrasenia, String correo, String nombreUsuario) {
        super(idPersona, nombre, apellidos, genero); // Ahora 'genero' es boolean
        this.idUsuario = idUsuario;
        this.contrasenia = contrasenia;
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", contrasenia='" + contrasenia + '\'' +
                ", correo='" + correo + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                "} " + super.toString();
    }
}
