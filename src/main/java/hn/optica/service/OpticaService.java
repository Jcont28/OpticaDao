package hn.optica.service;
import hn.optica.dao.*; import hn.optica.dao.jdbc.*; import hn.optica.model.*; import java.util.*;
public class OpticaService {
 public final UsuarioDAO usuarios=new UsuarioJdbcDAO(); public final ProductoDAO productos=new ProductoJdbcDAO(); public final MedicionDAO mediciones=new MedicionJdbcDAO(); public final VentaDAO ventas=new VentaJdbcDAO();
 public List<Map<String,Object>> medicionesCliente(int cliente)throws Exception{return mediciones.porCliente(cliente);}
}
