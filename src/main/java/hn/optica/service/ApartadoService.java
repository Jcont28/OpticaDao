package hn.optica.service;

import hn.optica.dao.ApartadoDAO;
import hn.optica.dao.ProductoDAO;
import hn.optica.dao.jdbc.ApartadoJdbcDAO;
import hn.optica.dao.jdbc.ProductoJdbcDAO;
import hn.optica.model.Apartado;
import hn.optica.model.Producto;
import java.sql.SQLException;
import java.util.List;

public class ApartadoService {

    private ApartadoDAO apartadoDAO = new ApartadoJdbcDAO();
    private ProductoDAO productoDAO = new ProductoJdbcDAO();

    public void apartar(int clienteId, int aroId) throws SQLException {
        Producto aro = productoDAO.buscarPorId(aroId);
        if (aro == null || !Producto.ARO.equals(aro.getTipo())) {
            throw new IllegalArgumentException("El aro seleccionado no existe");
        }
        if (aro.getStock() <= 0) {
            throw new IllegalArgumentException("El aro no tiene unidades disponibles");
        }
        if (apartadoDAO.existePendiente(clienteId, aroId)) {
            throw new IllegalArgumentException("Ya tienes este aro apartado");
        }
        Apartado apartado = new Apartado();
        apartado.setClienteId(clienteId);
        apartado.setAroId(aroId);
        apartadoDAO.guardar(apartado);
    }

    public List<Apartado> listarPorCliente(int clienteId) throws SQLException {
        return apartadoDAO.listarPorCliente(clienteId);
    }

    public List<Apartado> listarPendientes() throws SQLException {
        return apartadoDAO.listarPendientes();
    }
}
