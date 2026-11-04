package hn.optica.dao;

import hn.optica.model.Usuario;
import hn.optica.model.Vendedor;
import java.sql.SQLException;

public interface VendedorDAO {

    int registrar(Usuario usuario, Vendedor vendedor) throws SQLException;

    Vendedor buscarPorUsuarioId(int usuarioId) throws SQLException;
}
