package hn.optica.dao;

import hn.optica.model.Aro;
import java.sql.SQLException;
import java.util.List;

public interface AroDAO {

    List<Aro> listarDisponibles(String marca, String material, String forma, Double precioMin, Double precioMax) throws SQLException;
}
