package hn.optica.dao.jdbc;
import hn.optica.config.DB; 
import hn.optica.dao.MedicionDAO; 
import java.sql.*; 
import java.util.*;
public class MedicionJdbcDAO implements MedicionDAO {
 public List<Map<String,Object>> porCliente(int id)
         throws Exception{List<Map<String,Object>> a=new ArrayList<>();
         try(Connection c=DB.get();PreparedStatement p=c.prepareStatement("SELECT MedicionID,Fecha,Ojo,Esfera,Cilindro,Eje,Adicion,Observaciones FROM Mediciones WHERE ClienteID=? ORDER BY Fecha DESC")){p.setInt(1,id);
         try(ResultSet r=p.executeQuery()){while(r.next()){Map<String,Object> m=new LinkedHashMap<>();
         for(String k:new String[]{"MedicionID","Fecha","Ojo","Esfera","Cilindro","Eje","Adicion","Observaciones"})m.put(k,r.getObject(k));a.add(m);}}}
         return a;}
}
