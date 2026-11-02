package hn.optica.controller;
import hn.optica.service.OpticaService;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.util.*;
@WebServlet("/api/mediciones")
public class MedicionServlet extends JsonServlet{
 private final OpticaService s=new OpticaService();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{if(!cliente(q)){p.sendError(403);
         return;}
         try{int uid=(int)q.getSession().getAttribute("usuarioId");int clienteId;
         try(var c=hn.optica.config.DB.get();var st=c.prepareStatement("SELECT ClienteID FROM Clientes WHERE UsuarioID=?")){st.setInt(1,uid);
         try(var r=st.executeQuery()){if(!r.next())
             throw new Exception("Perfil de cliente no encontrado");clienteId=r.getInt(1);}}json(p,s.medicionesCliente(clienteId));}
         catch(Exception e){p.setStatus(400);json(p,Map.of("mensaje",e.getMessage()));}}
}
