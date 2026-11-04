package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.VentaDAO;
import hn.optica.model.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentaJdbcDAO implements VentaDAO {

    @Override
    public int registrar(Venta v) throws SQLException {
        String sqlVenta = "INSERT INTO Ventas (ClienteID, MedicionID, AroID, LunaID, VendedorID, Cantidad, "
                + "PrecioAro, PrecioLuna, Subtotal, Impuesto, Total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlApartado = "UPDATE Apartados SET Estado = 'VENDIDO' WHERE ClienteID = ? AND AroID = ? AND Estado = 'PENDIENTE'";
        Connection con = DB.getConexion();
        try {
            con.setAutoCommit(false);
            descontarStock(con, v.getAroId(), v.getCantidad());
            descontarStock(con, v.getLunaId(), v.getCantidad());
            int ventaId;
            try (PreparedStatement ps = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, v.getClienteId());
                ps.setInt(2, v.getMedicionId());
                ps.setInt(3, v.getAroId());
                ps.setInt(4, v.getLunaId());
                ps.setInt(5, v.getVendedorId());
                ps.setInt(6, v.getCantidad());
                ps.setDouble(7, v.getPrecioAro());
                ps.setDouble(8, v.getPrecioLuna());
                ps.setDouble(9, v.getSubtotal());
                ps.setDouble(10, v.getImpuesto());
                ps.setDouble(11, v.getTotal());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    ventaId = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlApartado)) {
                ps.setInt(1, v.getClienteId());
                ps.setInt(2, v.getAroId());
                ps.executeUpdate();
            }
            con.commit();
            return ventaId;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    private void descontarStock(Connection con, int productoId, int cantidad) throws SQLException {
        String sql = "UPDATE Productos SET Stock = Stock - ? WHERE ProductoID = ? AND Activo = 1 AND Stock >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, productoId);
            ps.setInt(3, cantidad);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("Stock insuficiente para el producto con codigo " + productoId);
            }
        }
    }

    @Override
    public List<Venta> listar() throws SQLException {
        String sql = "SELECT v.VentaID, v.ClienteID, c.Nombre AS ClienteNombre, v.MedicionID, "
                + "v.AroID, a.Nombre AS AroNombre, v.LunaID, l.Nombre AS LunaNombre, "
                + "v.VendedorID, ve.Nombre AS VendedorNombre, v.Cantidad, v.PrecioAro, v.PrecioLuna, "
                + "v.Subtotal, v.Impuesto, v.Total, CONVERT(VARCHAR(16), v.Fecha, 120) AS Fecha "
                + "FROM Ventas v "
                + "INNER JOIN Clientes c ON c.ClienteID = v.ClienteID "
                + "INNER JOIN Productos a ON a.ProductoID = v.AroID "
                + "INNER JOIN Productos l ON l.ProductoID = v.LunaID "
                + "INNER JOIN Vendedores ve ON ve.VendedorID = v.VendedorID "
                + "ORDER BY v.VentaID DESC";
        List<Venta> lista = new ArrayList<>();
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("VentaID"));
                v.setClienteId(rs.getInt("ClienteID"));
                v.setClienteNombre(rs.getString("ClienteNombre"));
                v.setMedicionId(rs.getInt("MedicionID"));
                v.setAroId(rs.getInt("AroID"));
                v.setAroNombre(rs.getString("AroNombre"));
                v.setLunaId(rs.getInt("LunaID"));
                v.setLunaNombre(rs.getString("LunaNombre"));
                v.setVendedorId(rs.getInt("VendedorID"));
                v.setVendedorNombre(rs.getString("VendedorNombre"));
                v.setCantidad(rs.getInt("Cantidad"));
                v.setPrecioAro(rs.getDouble("PrecioAro"));
                v.setPrecioLuna(rs.getDouble("PrecioLuna"));
                v.setSubtotal(rs.getDouble("Subtotal"));
                v.setImpuesto(rs.getDouble("Impuesto"));
                v.setTotal(rs.getDouble("Total"));
                v.setFecha(rs.getString("Fecha"));
                lista.add(v);
            }
        }
        return lista;
    }
}
