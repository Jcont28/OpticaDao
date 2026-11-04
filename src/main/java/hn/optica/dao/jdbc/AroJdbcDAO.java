package hn.optica.dao.jdbc;

import hn.optica.config.DB;
import hn.optica.dao.AroDAO;
import hn.optica.model.Aro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AroJdbcDAO implements AroDAO {

    @Override
    public List<Aro> listarDisponibles(String marca, String material, String forma, Double precioMin, Double precioMax) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT ProductoID, Nombre, Tipo, Marca, Material, Forma, Precio, Stock "
                + "FROM Productos WHERE Activo = 1 AND Tipo = 'ARO' AND Stock > 0");
        List<Object> parametros = new ArrayList<>();
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
        sql.append(" ORDER BY Precio");

        List<Aro> lista = new ArrayList<>();
        try (Connection con = DB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Aro a = new Aro();
                    a.setId(rs.getInt("ProductoID"));
                    a.setNombre(rs.getString("Nombre"));
                    a.setMarca(rs.getString("Marca"));
                    a.setMaterial(rs.getString("Material"));
                    a.setForma(rs.getString("Forma"));
                    a.setPrecio(rs.getDouble("Precio"));
                    a.setStock(rs.getInt("Stock"));
                    lista.add(a);
                }
            }
        }
        return lista;
    }
}
