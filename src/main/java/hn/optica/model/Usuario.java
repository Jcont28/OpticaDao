package hn.optica.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String usuario;
    private transient String passwordHash;
    private String rol;
    private String nombre;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(String usuario, String passwordHash, String rol) {
        this.usuario = usuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
