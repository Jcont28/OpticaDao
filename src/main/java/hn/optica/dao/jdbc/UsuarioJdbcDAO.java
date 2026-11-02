package hn.optica.dao.jdbc;
import hn.optica.config.DB; 
import hn.optica.dao.UsuarioDAO; 
import hn.optica.model.Usuario;
import java.sql.*; 
import org.mindrot.jbcrypt.BCrypt;
public class UsuarioJdbcDAO implements UsuarioDAO {
 public Usuario autenticar(String user,String password)throws Exception{
  try(Connection c=DB.get();PreparedStatement p=c.prepareStatement("SELECT UsuarioID,usuario,password, Nombre_Usuario FROM usuariosoptica WHERE usuario=? AND Activo=1")){
   p.setString(1,user);try(ResultSet r=p.executeQuery()){if(!r.next()||!BCrypt.checkpw(password,r.getString("paswword")))
       return null;return new Usuario(r.getInt(1),r.getString(2),r.getString(3));}
  }
 }
 public boolean crearCliente(String nombre,String dni,String tel,String correo,String hash)throws Exception{
  try(Connection c=DB.get()){c.setAutoCommit(false);try{
   int uid;try(PreparedStatement p=c.prepareStatement("INSERT Usuarios(Usuario,PasswordHash,Rol) VALUES(?,?,'CLIENTE')",Statement.RETURN_GENERATED_KEYS)){p.setString(1,correo);p.setString(2,hash);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();uid=r.getInt(1);}}
   try(PreparedStatement p=c.prepareStatement("INSERT Clientes(UsuarioID,Nombre,DNI,Telefono,Correo) VALUES(?,?,?,?,?)")){p.setInt(1,uid);p.setString(2,nombre);p.setString(3,dni);p.setString(4,tel);p.setString(5,correo);p.executeUpdate();}
   c.commit();return true;
  }catch(Exception e){c.rollback();throw e;}}
 }
}
