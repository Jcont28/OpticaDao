package hn.optica.controller;

import com.google.gson.JsonObject;
import hn.optica.model.Cliente;
import hn.optica.model.Medicion;
import hn.optica.service.MedicionService;
import hn.optica.service.UsuarioService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/mediciones")
public class MedicionServlet extends JsonServlet {

    private MedicionService medicionService = new MedicionService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (usuarioSesion(req) == null) {
            enviarMensaje(resp, 401, "Debe iniciar sesion");
            return;
        }
        try {
            int clienteId;
            if (esCliente(req)) {
                Cliente cliente = usuarioService.buscarClientePorUsuario(usuarioSesion(req).getId());
                clienteId = cliente.getId();
            } else {
                clienteId = parametroEntero(req, "clienteId");
            }
            enviarJson(resp, 200, medicionService.listarPorCliente(clienteId));
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!permitirVendedor(req, resp)) {
            return;
        }
        try {
            JsonObject datos = leerJson(req);
            Medicion m = new Medicion();
            m.setClienteId(entero(datos, "clienteId"));
            m.setEsferaOd(decimal(datos, "esferaOd"));
            m.setCilindroOd(decimal(datos, "cilindroOd"));
            m.setEjeOd(entero(datos, "ejeOd"));
            m.setEsferaOi(decimal(datos, "esferaOi"));
            m.setCilindroOi(decimal(datos, "cilindroOi"));
            m.setEjeOi(entero(datos, "ejeOi"));
            m.setAdicion(decimal(datos, "adicion"));
            m.setDistanciaPupilar(decimal(datos, "distanciaPupilar"));
            m.setObservaciones(texto(datos, "observaciones"));
            medicionService.guardar(m);
            enviarMensaje(resp, 200, "Medicion registrada");
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }
}
