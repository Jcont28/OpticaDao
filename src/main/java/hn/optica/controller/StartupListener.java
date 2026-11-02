package hn.optica.controller;
import jakarta.servlet.*;import jakarta.servlet.annotation.WebListener;import hn.optica.config.DB;import org.mindrot.jbcrypt.BCrypt;import java.sql.*;
@WebListener
public class StartupListener implements ServletContextListener{
 public void contextInitialized(ServletContextEvent e){try(Connection c=DB.get()){
  try(PreparedStatement q=c.prepareStatement("SELECT UsuarioID FROM Usuarios WHERE Usuario='vendedor@optica.local'");ResultSet r=q.executeQuery()){
      if(!r.next()){
   int id;
   try(PreparedStatement p=c.prepareStatement("INSERT Usuarios(Usuario,PasswordHash,Rol) VALUES(?,?,'VENDEDOR')",Statement.RETURN_GENERATED_KEYS)){p.setString(1,"vendedor@optica.local");p.setString(2,BCrypt.hashpw("Admin123*",BCrypt.gensalt()));p.executeUpdate();
   try(ResultSet k=p.getGeneratedKeys()){k.next();id=k.getInt(1);}}
   try(PreparedStatement p=c.prepareStatement("INSERT Vendedores(UsuarioID,Nombre) VALUES(?,'Vendedor Demo')")){p.setInt(1,id);p.executeUpdate();}
  }}
 }catch(Exception ex){e.getServletContext().log("No se pudo inicializar usuario demo; verifica la conexión y ejecuta optica.sql",ex);}}
}
