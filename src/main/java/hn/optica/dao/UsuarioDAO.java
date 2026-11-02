package hn.optica.dao;
import hn.optica.model.Usuario;
public interface UsuarioDAO { Usuario autenticar(String usuario,String password) throws Exception; boolean crearCliente(String nombre,String dni,String telefono,String correo,String hash) throws Exception; }
