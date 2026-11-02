package hn.optica.controller;
import hn.optica.config.DB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;import java.sql.Connection;
import java.util.*;
@WebServlet("/api/apartados")
public class ApartadoServlet extends JsonServlet{
 protected void doPost(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{if(!cliente(q)){p.sendError(403);
         return;}
         try{int uid=(int)q.getSession().getAttribute("usuarioId"),aro=num(body(q),"aroId");
         try(Connection c=DB.get();var s=c.prepareStatement("INSERT Apartados(ClienteID,AroID) SELECT ClienteID,? FROM Clientes WHERE UsuarioID=?"))
         {s.setInt(1,aro);s.setInt(2,uid);s.executeUpdate();}
         json(p,Map.of("mensaje","Aro apartado correctamente"));}
         catch(Exception e){p.setStatus(400);json(p,Map.of("mensaje",e.getMessage()));}}
}
