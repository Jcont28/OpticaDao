package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.ClienteDAO;
import hn.optica.model.Cliente;
import hn.optica.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteJdbcDAO implements ClienteDAO {

    @Override
    public int registrar(Usuario usuario, Cliente cliente) throws SQLException {
        String sqlUsuario = "INSERT INTO Usuarios (Usuario, PasswordHash, Rol) VALUES (?, ?, ?)";
        String sqlCliente = "INSERT INTO Clientes (UsuarioID, Nombre, DNI, Telefono, Correo) VALUES (?, ?, ?, ?, ?)";
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
            int clienteId;
            try (PreparedStatement ps = con.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, usuarioId);
                ps.setString(2, cliente.getNombre());
                ps.setString(3, cliente.getDni());
                ps.setString(4, cliente.getTelefono());
                ps.setString(5, cliente.getCorreo());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    clienteId = rs.getInt(1);
                }
            }
            con.commit();
            return clienteId;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT ClienteID, UsuarioID, Nombre, DNI, Telefono, Correo FROM Clientes ORDER BY Nombre";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(leerCliente(rs));
            }
        }
        return lista;
    }

    @Override
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT ClienteID, UsuarioID, Nombre, DNI, Telefono, Correo FROM Clientes WHERE ClienteID = ?";
        return buscarUno(sql, id);
    }

    @Override
    public Cliente buscarPorUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT ClienteID, UsuarioID, Nombre, DNI, Telefono, Correo FROM Clientes WHERE UsuarioID = ?";
        return buscarUno(sql, usuarioId);
    }

    @Override
    public boolean existeDni(String dni) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Clientes WHERE DNI = ?";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private Cliente buscarUno(String sql, int valor) throws SQLException {
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return leerCliente(rs);
                }
            }
        }
        return null;
    }

    private Cliente leerCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("ClienteID"));
        c.setUsuarioId(rs.getInt("UsuarioID"));
        c.setNombre(rs.getString("Nombre"));
        c.setDni(rs.getString("DNI"));
        c.setTelefono(rs.getString("Telefono"));
        c.setCorreo(rs.getString("Correo"));
        return c;
    }
}
