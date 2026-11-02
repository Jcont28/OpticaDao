package hn.optica.controller;
import hn.optica.config.DB;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.sql.Connection;
import java.util.*;
@WebServlet("/api/opciones")
public class LookupServlet extends JsonServlet{
 @Override
 protected void doGet(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{if(!vendedor(q)){p.sendError(403);
         return;}try(Connection c=DB.get()){String sql="";
         switch(q.getParameter("tipo"))
         {case "clientes"->sql="SELECT ClienteID id,Nombre nombre FROM Clientes";
         case "mediciones"->sql="SELECT MedicionID id,CONCAT('Medición ',MedicionID,' - ',Fecha,' / ',Ojo) nombre FROM Mediciones WHERE ClienteID="+Integer.parseInt(q.getParameter("clienteId"));
         case "vendedor"->sql="SELECT VendedorID id,Nombre nombre FROM Vendedores WHERE UsuarioID="+(int)q.getSession().getAttribute("usuarioId");
         default->throw new Exception("Consulta no válida");}List<Map<String,Object>> a=new ArrayList<>();
         try(var st=c.createStatement();var r=st.executeQuery(sql))
         {while(r.next())a.add(Map.of("id",r.getInt("id"),"nombre",r.getString("nombre")));}json(p,a);}
         catch(Exception e){p.setStatus(400);json(p,Map.of("mensaje",e.getMessage()));}}
}
