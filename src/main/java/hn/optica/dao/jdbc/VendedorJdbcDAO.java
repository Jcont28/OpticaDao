package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.VendedorDAO;
import hn.optica.model.Usuario;
import hn.optica.model.Vendedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VendedorJdbcDAO implements VendedorDAO {

    @Override
    public int registrar(Usuario usuario, Vendedor vendedor) throws SQLException {
        String sqlUsuario = "INSERT INTO Usuarios (Usuario, PasswordHash, Rol) VALUES (?, ?, ?)";
        String sqlVendedor = "INSERT INTO Vendedores (UsuarioID, Nombre) VALUES (?, ?)";
        Connection con = DB.getConexion();
        try {
            con.setAutoCommit(false);
            int usuarioId;
            try (PreparedStatement ps = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getUsuario());
                ps.setString(2, usuario.getPasswordHash());
                ps.setString(3, usuario.getRol());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    usuarioId = rs.getInt(1);
                }
            }
            int vendedorId;
            try (PreparedStatement ps = con.prepareStatement(sqlVendedor, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, usuarioId);
                ps.setString(2, vendedor.getNombre());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    vendedorId = rs.getInt(1);
                }
            }
            con.commit();
            return vendedorId;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    @Override
    public Vendedor buscarPorUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT VendedorID, UsuarioID, Nombre FROM Vendedores WHERE UsuarioID = ?";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vendedor v = new Vendedor();
                    v.setId(rs.getInt("VendedorID"));
                    v.setUsuarioId(rs.getInt("UsuarioID"));
                    v.setNombre(rs.getString("Nombre"));
                    return v;
                }
            }
        }
        return null;
    }
}
