package hn.optica.controller;

import com.google.gson.JsonObject;
import hn.optica.model.Vendedor;
import hn.optica.model.Venta;
import hn.optica.service.UsuarioService;
import hn.optica.service.VentaService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/ventas")
public class VentaServlet extends JsonServlet {

    private VentaService ventaService = new VentaService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!permitirVendedor(req, resp)) {
            return;
        }
        try {
            enviarJson(resp, 200, ventaService.listar());
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
            Vendedor vendedor = usuarioService.buscarVendedorPorUsuario(usuarioSesion(req).getId());
            Venta venta = ventaService.registrar(
                    entero(datos, "clienteId"),
                    entero(datos, "medicionId"),
                    entero(datos, "aroId"),
                    entero(datos, "lunaId"),
                    vendedor.getId(),
                    entero(datos, "cantidad"),
                    decimal(datos, "impuesto"));
            enviarJson(resp, 200, venta);
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }
}
