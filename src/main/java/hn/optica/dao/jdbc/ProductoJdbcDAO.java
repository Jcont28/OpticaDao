package hn.optica.dao.jdbc;
import hn.optica.config.DB; 
import hn.optica.dao.ProductoDAO; 
import hn.optica.model.Producto;
import java.sql.*; 
import java.util.*;
public class ProductoJdbcDAO implements ProductoDAO {
 public List<Producto> listar(String tipo,String marca,String material,String forma)throws Exception{
  List<Producto> out=new ArrayList<>();String q="SELECT * FROM Productos WHERE Activo=1 AND (? IS NULL OR Tipo=?) AND (? IS NULL OR Marca LIKE ?) AND (? IS NULL OR Material LIKE ?) AND (? IS NULL OR Forma LIKE ?)";
  try(Connection c=DB.get();PreparedStatement p=c.prepareStatement(q)){String[] a={tipo,marca,material,forma};int i=1;for(String s:a){p.setString(i++,s);p.setString(i++,s==null?null:"%"+s+"%");}try(ResultSet r=p.executeQuery()){while(r.next()){Producto x=new Producto();x.id=r.getInt("ProductoID");x.nombre=r.getString("Nombre");x.tipo=r.getString("Tipo");x.marca=r.getString("Marca");x.material=r.getString("Material");x.forma=r.getString("Forma");x.precio=r.getBigDecimal("Precio");x.stock=r.getInt("Stock");out.add(x);}}}return out;
 }
 public void guardar(Producto x)
         throws Exception{
     try(Connection c=DB.get();PreparedStatement p=c.prepareStatement("INSERT Productos(Nombre,Tipo,Marca,Material,Forma,Precio,Stock) VALUES(?,?,?,?,?,?,?)")){bind(p,x);p.executeUpdate();}}
 public void actualizar(Producto x)throws Exception{try(Connection c=DB.get();PreparedStatement p=c.prepareStatement("UPDATE Productos SET Nombre=?,Tipo=?,Marca=?,Material=?,Forma=?,Precio=?,Stock=? WHERE ProductoID=?")){bind(p,x);p.setInt(8,x.id);p.executeUpdate();}}
 private void bind(PreparedStatement p,Producto x)throws SQLException{p.setString(1,x.nombre);p.setString(2,x.tipo);p.setString(3,x.marca);p.setString(4,x.material);p.setString(5,x.forma);p.setBigDecimal(6,x.precio);p.setInt(7,x.stock);}
 public void desactivar(int id)throws Exception{try(Connection c=DB.get();PreparedStatement p=c.prepareStatement("UPDATE Productos SET Activo=0 WHERE ProductoID=?")){p.setInt(1,id);p.executeUpdate();}}
}
