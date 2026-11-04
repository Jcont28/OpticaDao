package hn.optica.controller;

import hn.optica.service.UsuarioService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/clientes")
public class ClienteServlet extends JsonServlet {

    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!permitirVendedor(req, resp)) {
            return;
        }
        try {
            enviarJson(resp, 200, usuarioService.listarClientes());
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }
}
