package hn.optica.dao;

import hn.optica.model.Cliente;
import hn.optica.model.Usuario;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {

    int registrar(Usuario usuario, Cliente cliente) throws SQLException;

    List<Cliente> listar() throws SQLException;

    Cliente buscarPorId(int id) throws SQLException;

    Cliente buscarPorUsuarioId(int usuarioId) throws SQLException;

    boolean existeDni(String dni) throws SQLException;
}
