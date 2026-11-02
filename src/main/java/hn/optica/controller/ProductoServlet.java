package hn.optica.controller;
import hn.optica.service.OpticaService;import hn.optica.model.Producto;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import com.google.gson.JsonObject;import java.math.BigDecimal;import java.util.*;
@WebServlet("/api/productos")
public class ProductoServlet extends JsonServlet{
 private final OpticaService s=new OpticaService();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{
     if(!cliente(q)&&!vendedor(q)){p.sendError(401);
     return;}try{json(p,s.productos.listar(q.getParameter("tipo"),q.getParameter("marca"),q.getParameter("material"),q.getParameter("forma")));}
     catch(Exception e){p.setStatus(500);json(p,Map.of("mensaje",e.getMessage()));}}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{if(!vendedor(q)){p.sendError(403);
         return;}
         try{Producto x=leer(body(q));s.productos.guardar(x);json(p,Map.of("mensaje","Producto guardado"));}
         catch(Exception e){p.setStatus(400);json(p,Map.of("mensaje",e.getMessage()));}}
 protected void doPut(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{if(!vendedor(q)){p.sendError(403);
         return;}
         try{JsonObject b=body(q);Producto x=leer(b);x.id=num(b,"id");s.productos.actualizar(x);json(p,Map.of("mensaje","Producto actualizado"));}
         catch(Exception e){p.setStatus(400);json(p,Map.of("mensaje",e.getMessage()));}}
 protected void doDelete(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{if(!vendedor(q)){p.sendError(403);
         return;}
         try{s.productos.desactivar(Integer.parseInt(q.getParameter("id")));json(p,Map.of("mensaje","Producto desactivado"));}
         catch(Exception e){p.setStatus(400);json(p,Map.of("mensaje",e.getMessage()));}}
 private Producto leer(JsonObject b){Producto x=new Producto();x.nombre=str(b,"nombre");x.tipo=str(b,"tipo");x.marca=str(b,"marca");x.material=str(b,"material");x.forma=str(b,"forma");x.precio=new BigDecimal(b.get("precio").getAsString());x.stock=num(b,"stock");
 return x;}
}
