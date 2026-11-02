package hn.optica.model;

public class Usuario { 
    public int id; 
    public String usuario, Nombre_Usuario, rol;

    // Se añadió 'String rol' en los parámetros del constructor
    public Usuario(int id, String usuario, String Nombre_Usuario) {
        this.id = id;
        this.usuario = usuario;
        this.Nombre_Usuario = Nombre_Usuario;
        this.rol = rol;
    } 
}
