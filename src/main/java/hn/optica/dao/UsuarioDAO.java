package hn.optica.dao;

import hn.optica.model.Usuario;
import java.sql.SQLException;

public interface UsuarioDAO {

    Usuario buscarPorUsuario(String usuario) throws SQLException;
}
