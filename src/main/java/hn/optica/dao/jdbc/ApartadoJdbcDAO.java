package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.ApartadoDAO;
import hn.optica.model.Apartado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ApartadoJdbcDAO implements ApartadoDAO {

    private static final String SELECT = "SELECT a.ApartadoID, a.ClienteID, c.Nombre AS ClienteNombre, a.AroID, "
            + "p.Nombre AS AroNombre, p.Marca, p.Precio, CONVERT(VARCHAR(16), a.Fecha, 120) AS Fecha, a.Estado "
            + "FROM Apartados a "
            + "INNER JOIN Clientes c ON c.ClienteID = a.ClienteID "
            + "INNER JOIN Productos p ON p.ProductoID = a.AroID ";

    @Override
    public int guardar(Apartado apartado) throws SQLException {
        String sql = "INSERT INTO Apartados (ClienteID, AroID) VALUES (?, ?)";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, apartado.getClienteId());
            ps.setInt(2, apartado.getAroId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    @Override
    public List<Apartado> listarPorCliente(int clienteId) throws SQLException {
        String sql = SELECT + "WHERE a.ClienteID = ? ORDER BY a.Fecha DESC";
        List<Apartado> lista = new ArrayList<>();
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leerApartado(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Apartado> listarPendientes() throws SQLException {
        String sql = SELECT + "WHERE a.Estado = 'PENDIENTE' ORDER BY a.Fecha DESC";
        List<Apartado> lista = new ArrayList<>();
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(leerApartado(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean existePendiente(int clienteId, int aroId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Apartados WHERE ClienteID = ? AND AroID = ? AND Estado = 'PENDIENTE'";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, aroId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private Apartado leerApartado(ResultSet rs) throws SQLException {
        Apartado a = new Apartado();
        a.setId(rs.getInt("ApartadoID"));
        a.setClienteId(rs.getInt("ClienteID"));
        a.setClienteNombre(rs.getString("ClienteNombre"));
        a.setAroId(rs.getInt("AroID"));
        a.setAroNombre(rs.getString("AroNombre"));
        a.setMarca(rs.getString("Marca"));
        a.setPrecio(rs.getDouble("Precio"));
        a.setFecha(rs.getString("Fecha"));
        a.setEstado(rs.getString("Estado"));
        return a;
    }
}
