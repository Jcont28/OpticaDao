package hn.optica.dao;

import hn.optica.model.Venta;
import java.sql.SQLException;
import java.util.List;

public interface VentaDAO {

    int registrar(Venta venta) throws SQLException;

    List<Venta> listar() throws SQLException;
}
