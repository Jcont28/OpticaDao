package hn.optica.dao;

import hn.optica.model.Apartado;
import java.sql.SQLException;
import java.util.List;

public interface ApartadoDAO {

    int guardar(Apartado apartado) throws SQLException;

    List<Apartado> listarPorCliente(int clienteId) throws SQLException;

    List<Apartado> listarPendientes() throws SQLException;

    boolean existePendiente(int clienteId, int aroId) throws SQLException;
}
