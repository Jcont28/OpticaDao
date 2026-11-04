package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.UsuarioDAO;
import hn.optica.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioJdbcDAO implements UsuarioDAO {

    @Override
    public Usuario buscarPorUsuario(String usuario) throws SQLException {
        String sql = "SELECT u.UsuarioID, u.Usuario, u.PasswordHash, u.Rol, u.Activo, "
                + "COALESCE(c.Nombre, v.Nombre) AS Nombre "
                + "FROM Usuarios u "
                + "LEFT JOIN Clientes c ON c.UsuarioID = u.UsuarioID "
                + "LEFT JOIN Vendedores v ON v.UsuarioID = u.UsuarioID "
                + "WHERE u.Usuario = ?";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("UsuarioID"));
                    u.setUsuario(rs.getString("Usuario"));
                    u.setPasswordHash(rs.getString("PasswordHash"));
                    u.setRol(rs.getString("Rol"));
                    u.setActivo(rs.getBoolean("Activo"));
                    u.setNombre(rs.getString("Nombre"));
                    return u;
                }
            }
        }
        return null;
    }
}
