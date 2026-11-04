package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.ProductoDAO;
import hn.optica.model.Aro;
import hn.optica.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoJdbcDAO implements ProductoDAO {

    private static final String SELECT = "SELECT ProductoID, Nombre, Tipo, Marca, Material, Forma, Precio, Stock FROM Productos ";

    @Override
    public List<Producto> listar(String tipo, String marca, String material, String forma, Double precioMin, Double precioMax) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT + "WHERE Activo = 1");
        List<Object> parametros = new ArrayList<>();
        if (tipo != null && !tipo.isEmpty()) {
            sql.append(" AND Tipo = ?");
            parametros.add(tipo);
        }
        if (marca != null && !marca.isEmpty()) {
            sql.append(" AND Marca LIKE ?");
            parametros.add("%" + marca + "%");
        }
        if (material != null && !material.isEmpty()) {
            sql.append(" AND Material LIKE ?");
            parametros.add("%" + material + "%");
        }
        if (forma != null && !forma.isEmpty()) {
            sql.append(" AND Forma LIKE ?");
            parametros.add("%" + forma + "%");
        }
        if (precioMin != null) {
            sql.append(" AND Precio >= ?");
            parametros.add(precioMin);
        }
        if (precioMax != null) {
            sql.append(" AND Precio <= ?");
            parametros.add(precioMax);
        }
        sql.append(" ORDER BY Tipo, Nombre");

        List<Producto> lista = new ArrayList<>();
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leerProducto(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public Producto buscarPorId(int id) throws SQLException {
        String sql = SELECT + "WHERE ProductoID = ? AND Activo = 1";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return leerProducto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public int guardar(Producto p) throws SQLException {
        String sql = "INSERT INTO Productos (Nombre, Tipo, Marca, Material, Forma, Precio, Stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getMarca());
            ps.setString(4, p.getMaterial());
            ps.setString(5, p.getForma());
            ps.setDouble(6, p.getPrecio());
            ps.setInt(7, p.getStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    @Override
    public boolean actualizar(Producto p) throws SQLException {
        String sql = "UPDATE Productos SET Nombre = ?, Tipo = ?, Marca = ?, Material = ?, Forma = ?, Precio = ?, Stock = ? "
                + "WHERE ProductoID = ? AND Activo = 1";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getMarca());
            ps.setString(4, p.getMaterial());
            ps.setString(5, p.getForma());
            ps.setDouble(6, p.getPrecio());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE Productos SET Activo = 0 WHERE ProductoID = ?";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Producto> listarStockBajo(String tipo, int limite) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = SELECT + "WHERE Activo = 1 AND Tipo = ? AND Stock < ? ORDER BY Stock";
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setInt(2, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leerProducto(rs));
                }
            }
        }
        return lista;
    }

    private Producto leerProducto(ResultSet rs) throws SQLException {
        Producto p;
        if (Producto.ARO.equals(rs.getString("Tipo"))) {
            p = new Aro();
        } else {
            p = new Producto();
        }
        p.setId(rs.getInt("ProductoID"));
        p.setNombre(rs.getString("Nombre"));
        p.setTipo(rs.getString("Tipo"));
        p.setMarca(rs.getString("Marca"));
        p.setMaterial(rs.getString("Material"));
        p.setForma(rs.getString("Forma"));
        p.setPrecio(rs.getDouble("Precio"));
        p.setStock(rs.getInt("Stock"));
        return p;
    }
}
