package hn.optica.controller;
import hn.optica.service.OpticaService;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.util.*;
@WebServlet("/api/ventas")
public class VentaServlet extends JsonServlet{
 private final OpticaService s=new OpticaService();
 @Override
 protected void doPost(HttpServletRequest q,HttpServletResponse p)
         throws java.io.IOException{if(!vendedor(q)){p.sendError(403);
         return;}
         try{var b=body(q);int id=s.ventas.registrar(num(b,"clienteId"),num(b,"medicionId"),num(b,"aroId"),num(b,"lunaId"),num(b,"vendedorId"),num(b,"cantidad"),b.has("impuesto")?b.get("impuesto").getAsDouble():0);json(p,Map.of("mensaje","Venta registrada correctamente","ventaId",id));}
         catch(Exception e){p.setStatus(400);json(p,Map.of("mensaje",e.getMessage()));}}
}
