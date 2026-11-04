package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.MedicionDAO;
import hn.optica.model.Medicion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MedicionJdbcDAO implements MedicionDAO {

    private static final String SELECT = "SELECT m.MedicionID, m.ClienteID, c.Nombre AS ClienteNombre, "
            + "CONVERT(VARCHAR(10), m.Fecha, 23) AS Fecha, m.EsferaOD, m.CilindroOD, m.EjeOD, "
            + "m.EsferaOI, m.CilindroOI, m.EjeOI, m.Adicion, m.DistanciaPupilar, m.Observaciones "
            + "FROM Mediciones m INNER JOIN Clientes c ON c.ClienteID = m.ClienteID ";

    @Override
    public List<Medicion> listarPorCliente(int clienteId) throws SQLException {
        List<Medicion> lista = new ArrayList<>();
        String sql = SELECT + "WHERE m.ClienteID = ? ORDER BY m.Fecha DESC, m.MedicionID DESC";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leerMedicion(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public Medicion buscarPorId(int id) throws SQLException {
        String sql = SELECT + "WHERE m.MedicionID = ?";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return leerMedicion(rs);
                }
            }
        }
        return null;
    }

    @Override
    public int guardar(Medicion m) throws SQLException {
        String sql = "INSERT INTO Mediciones (ClienteID, EsferaOD, CilindroOD, EjeOD, EsferaOI, CilindroOI, EjeOI, "
                + "Adicion, DistanciaPupilar, Observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getClienteId());
            ps.setDouble(2, m.getEsferaOd());
            ps.setDouble(3, m.getCilindroOd());
            ps.setInt(4, m.getEjeOd());
            ps.setDouble(5, m.getEsferaOi());
            ps.setDouble(6, m.getCilindroOi());
            ps.setInt(7, m.getEjeOi());
            ps.setDouble(8, m.getAdicion());
            ps.setDouble(9, m.getDistanciaPupilar());
            ps.setString(10, m.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private Medicion leerMedicion(ResultSet rs) throws SQLException {
        Medicion m = new Medicion();
        m.setId(rs.getInt("MedicionID"));
        m.setClienteId(rs.getInt("ClienteID"));
        m.setClienteNombre(rs.getString("ClienteNombre"));
        m.setFecha(rs.getString("Fecha"));
        m.setEsferaOd(rs.getDouble("EsferaOD"));
        m.setCilindroOd(rs.getDouble("CilindroOD"));
        m.setEjeOd(rs.getInt("EjeOD"));
        m.setEsferaOi(rs.getDouble("EsferaOI"));
        m.setCilindroOi(rs.getDouble("CilindroOI"));
        m.setEjeOi(rs.getInt("EjeOI"));
        m.setAdicion(rs.getDouble("Adicion"));
        m.setDistanciaPupilar(rs.getDouble("DistanciaPupilar"));
        m.setObservaciones(rs.getString("Observaciones"));
        return m;
    }
}
