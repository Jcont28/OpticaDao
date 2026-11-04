package hn.optica.service;

import hn.optica.dao.ClienteDAO;
import hn.optica.dao.MedicionDAO;
import hn.optica.dao.jdbc.ClienteJdbcDAO;
import hn.optica.dao.jdbc.MedicionJdbcDAO;
import hn.optica.model.Medicion;
import java.sql.SQLException;
import java.util.List;

public class MedicionService {

    private MedicionDAO medicionDAO = new MedicionJdbcDAO();
    private ClienteDAO clienteDAO = new ClienteJdbcDAO();

    public List<Medicion> listarPorCliente(int clienteId) throws SQLException {
        return medicionDAO.listarPorCliente(clienteId);
    }

    public void guardar(Medicion m) throws SQLException {
        if (clienteDAO.buscarPorId(m.getClienteId()) == null) {
            throw new IllegalArgumentException("Seleccione un cliente valido");
        }
        if (m.getEjeOd() < 0 || m.getEjeOd() > 180 || m.getEjeOi() < 0 || m.getEjeOi() > 180) {
            throw new IllegalArgumentException("El eje debe estar entre 0 y 180");
        }
        if (m.getEsferaOd() < -30 || m.getEsferaOd() > 30 || m.getEsferaOi() < -30 || m.getEsferaOi() > 30) {
            throw new IllegalArgumentException("La esfera debe estar entre -30 y 30");
        }
        if (m.getAdicion() < 0 || m.getAdicion() > 4) {
            throw new IllegalArgumentException("La adicion debe estar entre 0 y 4");
        }
        medicionDAO.guardar(m);
    }
}
