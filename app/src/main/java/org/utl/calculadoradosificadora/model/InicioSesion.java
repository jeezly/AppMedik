package org.utl.calculadoradosificadora.model;

public class InicioSesion {
    private String usuario;
    private String contrasenia;

    public InicioSesion() {
    }

    public InicioSesion(String usuario, String contrasenia) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
