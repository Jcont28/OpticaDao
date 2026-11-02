package hn.optica.config;
import java.sql.*;
public final class DB {
 private DB(){}
 public static Connection get() throws SQLException {
  String url=System.getenv().getOrDefault("OPTICA_DB_URL","jdbc:sqlserver://localhost:1433;databaseName=Optica;encrypt=true;trustServerCertificate=true");
  String user=System.getenv().getOrDefault("OPTICA_DB_USER","Jonacont");
  String pass=System.getenv().getOrDefault("OPTICA_DB_PASSWORD","Jc282828");
  return DriverManager.getConnection(url,user,pass);
 }
}
