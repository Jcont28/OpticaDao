package hn.optica.controller;

import com.google.gson.JsonObject;
import hn.optica.model.Cliente;
import hn.optica.service.ApartadoService;
import hn.optica.service.UsuarioService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/apartados")
public class ApartadoServlet extends JsonServlet {

    private ApartadoService apartadoService = new ApartadoService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (usuarioSesion(req) == null) {
            enviarMensaje(resp, 401, "Debe iniciar sesion");
            return;
        }
        try {
            if (esCliente(req)) {
                Cliente cliente = usuarioService.buscarClientePorUsuario(usuarioSesion(req).getId());
                enviarJson(resp, 200, apartadoService.listarPorCliente(cliente.getId()));
            } else {
                enviarJson(resp, 200, apartadoService.listarPendientes());
            }
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!permitirCliente(req, resp)) {
            return;
        }
        try {
            JsonObject datos = leerJson(req);
            Cliente cliente = usuarioService.buscarClientePorUsuario(usuarioSesion(req).getId());
            apartadoService.apartar(cliente.getId(), entero(datos, "aroId"));
            enviarMensaje(resp, 200, "Aro apartado correctamente");
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }
}
