package hn.optica.service;

import hn.optica.dao.ClienteDAO;
import hn.optica.dao.MedicionDAO;
import hn.optica.dao.ProductoDAO;
import hn.optica.dao.VentaDAO;
import hn.optica.dao.jdbc.ClienteJdbcDAO;
import hn.optica.dao.jdbc.MedicionJdbcDAO;
import hn.optica.dao.jdbc.ProductoJdbcDAO;
import hn.optica.dao.jdbc.VentaJdbcDAO;
import hn.optica.model.Cliente;
import hn.optica.model.Medicion;
import hn.optica.model.Producto;
import hn.optica.model.Venta;
import java.sql.SQLException;
import java.util.List;

public class VentaService {

    private VentaDAO ventaDAO = new VentaJdbcDAO();
    private ClienteDAO clienteDAO = new ClienteJdbcDAO();
    private MedicionDAO medicionDAO = new MedicionJdbcDAO();
    private ProductoDAO productoDAO = new ProductoJdbcDAO();

    public Venta registrar(int clienteId, int medicionId, int aroId, int lunaId, int vendedorId, int cantidad, double porcentajeImpuesto) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Seleccione un cliente");
        }
        Medicion medicion = medicionDAO.buscarPorId(medicionId);
        if (medicion == null || medicion.getClienteId() != clienteId) {
            throw new IllegalArgumentException("Seleccione una medicion del cliente");
        }
        Producto aro = productoDAO.buscarPorId(aroId);
        if (aro == null || !Producto.ARO.equals(aro.getTipo())) {
            throw new IllegalArgumentException("Seleccione un aro");
        }
        Producto luna = productoDAO.buscarPorId(lunaId);
        if (luna == null || !Producto.LUNA.equals(luna.getTipo())) {
            throw new IllegalArgumentException("Seleccione una luna");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (aro.getStock() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock del aro " + aro.getNombre());
        }
        if (luna.getStock() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock de la luna " + luna.getNombre());
        }
        if (porcentajeImpuesto < 0 || porcentajeImpuesto > 100) {
            throw new IllegalArgumentException("El impuesto debe estar entre 0 y 100");
        }

        double subtotal = redondear((aro.getPrecio() + luna.getPrecio()) * cantidad);
        double impuesto = redondear(subtotal * porcentajeImpuesto / 100);
        double total = redondear(subtotal + impuesto);

        Venta venta = new Venta();
        venta.setClienteId(clienteId);
        venta.setClienteNombre(cliente.getNombre());
        venta.setMedicionId(medicionId);
        venta.setAroId(aroId);
        venta.setAroNombre(aro.getNombre());
        venta.setLunaId(lunaId);
        venta.setLunaNombre(luna.getNombre());
        venta.setVendedorId(vendedorId);
        venta.setCantidad(cantidad);
        venta.setPrecioAro(aro.getPrecio());
        venta.setPrecioLuna(luna.getPrecio());
        venta.setSubtotal(subtotal);
        venta.setImpuesto(impuesto);
        venta.setTotal(total);

        int id = ventaDAO.registrar(venta);
        venta.setId(id);
        return venta;
    }

    public List<Venta> listar() throws SQLException {
        return ventaDAO.listar();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
