package hn.optica.dao;
import hn.optica.model.Producto;
import java.util.List;
public interface ProductoDAO { List<Producto> listar(String tipo,String marca,String material,String forma) throws Exception; void guardar(Producto p) throws Exception; void actualizar(Producto p) throws Exception; void desactivar(int id) throws Exception; }
