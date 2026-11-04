package hn.optica.service;

import hn.optica.dao.ClienteDAO;
import hn.optica.dao.UsuarioDAO;
import hn.optica.dao.VendedorDAO;
import hn.optica.dao.jdbc.ClienteJdbcDAO;
import hn.optica.dao.jdbc.UsuarioJdbcDAO;
import hn.optica.dao.jdbc.VendedorJdbcDAO;
import hn.optica.model.Cliente;
import hn.optica.model.Usuario;
import hn.optica.model.Vendedor;
import java.sql.SQLException;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {

    public static final String ROL_CLIENTE = "CLIENTE";
    public static final String ROL_VENDEDOR = "VENDEDOR";

    private UsuarioDAO usuarioDAO = new UsuarioJdbcDAO();
    private ClienteDAO clienteDAO = new ClienteJdbcDAO();
    private VendedorDAO vendedorDAO = new VendedorJdbcDAO();

    public Usuario login(String usuario, String password) throws SQLException {
        if (usuario.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Ingrese su correo y su contrasena");
        }
        Usuario u = usuarioDAO.buscarPorUsuario(usuario.toLowerCase());
        if (u == null || !u.isActivo() || !passwordCorrecta(password, u.getPasswordHash())) {
            throw new IllegalArgumentException("Usuario o contrasena incorrectos");
        }
        return u;
    }

    private boolean passwordCorrecta(String password, String hash) {
        try {
            return BCrypt.checkpw(password, hash);
        } catch (Exception e) {
            return false;
        }
    }

    public void registrarCliente(String nombre, String dni, String telefono, String correo, String password) throws SQLException {
        if (nombre.isEmpty() || dni.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Nombre, DNI, correo y contrasena son obligatorios");
        }
        if (!correo.contains("@") || !correo.contains(".")) {
            throw new IllegalArgumentException("El correo no es valido");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("La contrasena debe tener al menos 8 caracteres");
        }
        correo = correo.toLowerCase();
        if (usuarioDAO.buscarPorUsuario(correo) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese correo");
        }
        if (clienteDAO.existeDni(dni)) {
            throw new IllegalArgumentException("Ya existe un cliente con ese DNI");
        }
        Usuario usuario = new Usuario(correo, BCrypt.hashpw(password, BCrypt.gensalt()), ROL_CLIENTE);
        Cliente cliente = new Cliente(nombre, dni, telefono, correo);
        clienteDAO.registrar(usuario, cliente);
    }

    public void crearVendedorDemo() throws SQLException {
        String correo = "vendedor@optica.local";
        if (usuarioDAO.buscarPorUsuario(correo) == null) {
            Usuario usuario = new Usuario(correo, BCrypt.hashpw("Admin123*", BCrypt.gensalt()), ROL_VENDEDOR);
            vendedorDAO.registrar(usuario, new Vendedor("Vendedor Demo"));
        }
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listar();
    }

    public Cliente buscarClientePorUsuario(int usuarioId) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorUsuarioId(usuarioId);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontro el perfil del cliente");
        }
        return cliente;
    }

    public Vendedor buscarVendedorPorUsuario(int usuarioId) throws SQLException {
        Vendedor vendedor = vendedorDAO.buscarPorUsuarioId(usuarioId);
        if (vendedor == null) {
            throw new IllegalArgumentException("No se encontro el perfil del vendedor");
        }
        return vendedor;
    }
}
