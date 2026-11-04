package hn.optica.service;

import hn.optica.dao.AroDAO;
import hn.optica.dao.ProductoDAO;
import hn.optica.dao.jdbc.AroJdbcDAO;
import hn.optica.dao.jdbc.ProductoJdbcDAO;
import hn.optica.model.Aro;
import hn.optica.model.Producto;
import java.sql.SQLException;
import java.util.List;

public class ProductoService {

    public static final int STOCK_MINIMO = 5;

    private ProductoDAO productoDAO = new ProductoJdbcDAO();
    private AroDAO aroDAO = new AroJdbcDAO();

    public List<Producto> listar(String tipo, String marca, String material, String forma, Double precioMin, Double precioMax) throws SQLException {
        return productoDAO.listar(tipo, marca, material, forma, precioMin, precioMax);
    }

    public List<Aro> catalogoAros(String marca, String material, String forma, Double precioMin, Double precioMax) throws SQLException {
        return aroDAO.listarDisponibles(marca, material, forma, precioMin, precioMax);
    }

    public Producto buscar(int id) throws SQLException {
        return productoDAO.buscarPorId(id);
    }

    public void guardar(Producto p) throws SQLException {
        validar(p);
        productoDAO.guardar(p);
    }

    public void actualizar(Producto p) throws SQLException {
        if (p.getId() <= 0) {
            throw new IllegalArgumentException("Seleccione el producto a modificar");
        }
        validar(p);
        if (!productoDAO.actualizar(p)) {
            throw new IllegalArgumentException("El producto no existe");
        }
    }

    public void eliminar(int id) throws SQLException {
        if (!productoDAO.eliminar(id)) {
            throw new IllegalArgumentException("El producto no existe");
        }
    }

    public List<Producto> arosConStockBajo() throws SQLException {
        return productoDAO.listarStockBajo(Producto.ARO, STOCK_MINIMO);
    }

    private void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        String tipo = p.getTipo();
        if (!Producto.ARO.equals(tipo) && !Producto.LUNA.equals(tipo) && !Producto.ACCESORIO.equals(tipo)) {
            throw new IllegalArgumentException("El tipo debe ser ARO, LUNA o ACCESORIO");
        }
        if (p.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (p.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
