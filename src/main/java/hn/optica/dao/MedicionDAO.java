package hn.optica.dao;

import hn.optica.model.Medicion;
import java.sql.SQLException;
import java.util.List;

public interface MedicionDAO {

    List<Medicion> listarPorCliente(int clienteId) throws SQLException;

    Medicion buscarPorId(int id) throws SQLException;

    int guardar(Medicion medicion) throws SQLException;
}
