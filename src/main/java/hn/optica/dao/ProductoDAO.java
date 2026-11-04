package hn.optica.dao;

import hn.optica.model.Producto;
import java.sql.SQLException;
import java.util.List;

public interface ProductoDAO {

    List<Producto> listar(String tipo, String marca, String material, String forma, Double precioMin, Double precioMax) throws SQLException;

    Producto buscarPorId(int id) throws SQLException;

    int guardar(Producto producto) throws SQLException;

    boolean actualizar(Producto producto) throws SQLException;

    boolean eliminar(int id) throws SQLException;

    List<Producto> listarStockBajo(String tipo, int limite) throws SQLException;
}
