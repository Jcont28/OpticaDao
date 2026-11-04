package hn.optica.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    private static String url;
    private static String usuario;
    private static String password;

    private DB() {
    }

    private static void cargarDatos() {
        if (url != null) {
            return;
        }
        String urlEntorno = System.getenv("jdbc:sqlserver://localhost:1433;databaseName=Optica;encrypt=true;trustServerCertificate=true");
        if (urlEntorno != null && !urlEntorno.isEmpty()) {
            url = urlEntorno;
            usuario = System.getenv("Jonacont");
            password = System.getenv("Jc282828");
            return;
        }
        Properties datos = new Properties();
        try (InputStream archivo = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (archivo != null) {
                datos.load(archivo);
            }
        } catch (Exception e) {
            System.out.println("No se pudo leer db.properties: " + e.getMessage());
        }
        url = datos.getProperty("db.url");
        usuario = datos.getProperty("db.user");
        password = datos.getProperty("db.password");
    }

    public static Connection getConexion() throws SQLException {
        cargarDatos();
        if (url == null || url.isEmpty()) {
            throw new SQLException("No hay datos de conexion. Configure OPTICA_DB_URL o el archivo db.properties");
        }
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de SQL Server");
        }
        return DriverManager.getConnection(url, usuario, password);
    }
}
