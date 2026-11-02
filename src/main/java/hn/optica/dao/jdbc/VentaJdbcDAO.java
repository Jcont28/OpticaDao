package hn.optica.dao.jdbc;
import hn.optica.config.DB; 
import hn.optica.dao.VentaDAO; 
import java.sql.*;
public class VentaJdbcDAO implements VentaDAO {
 public int registrar(int cliente,int medicion,int aro,int luna,int vendedor,int cantidad,double tasa)throws Exception{
  try(Connection c=DB.get()){c.setAutoCommit(false);try{
   validarProducto(c,aro,cantidad);validarProducto(c,luna,cantidad);
   double subtotal=precio(c,aro)*cantidad+precio(c,luna)*cantidad, impuesto=subtotal*tasa, total=subtotal+impuesto;
   try(PreparedStatement p=c.prepareStatement("INSERT Ventas(ClienteID,MedicionID,AroID,LunaID,VendedorID,Cantidad,Subtotal,Impuesto,Total) VALUES(?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
    p.setInt(1,cliente);p.setInt(2,medicion);p.setInt(3,aro);p.setInt(4,luna);p.setInt(5,vendedor);p.setInt(6,cantidad);p.setDouble(7,subtotal);p.setDouble(8,impuesto);p.setDouble(9,total);p.executeUpdate();
    try(ResultSet r=p.getGeneratedKeys()){r.next();
    int id=r.getInt(1);
     try(PreparedStatement u=c.prepareStatement("UPDATE Productos SET Stock=Stock-? WHERE ProductoID IN (?,?)")){u.setInt(1,cantidad);u.setInt(2,aro);u.setInt(3,luna);u.executeUpdate();}c.commit();return id;}
   }
  }catch(Exception e){c.rollback();throw e;}}
 }
 private void validarProducto(Connection c,int id,int qty)
         throws Exception{try(PreparedStatement p=c.prepareStatement("SELECT Stock,Tipo FROM Productos WITH(UPDLOCK,HOLDLOCK) WHERE ProductoID=? AND Activo=1")){p.setInt(1,id);
         try(ResultSet r=p.executeQuery()){
             if(!r.next()||r.getInt(1)<qty)throw new SQLException("Producto inexistente o stock insuficiente: "+id);}}}
 private double precio(Connection c,int id)
         throws Exception{
     try(PreparedStatement p=c.prepareStatement("SELECT Precio FROM Productos WHERE ProductoID=?")){p.setInt(1,id);
     try(ResultSet r=p.executeQuery()){r.next();
     return r.getDouble(1);}}}
}
