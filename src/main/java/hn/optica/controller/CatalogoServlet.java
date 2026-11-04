package hn.optica.controller;

import hn.optica.service.ProductoService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/catalogo")
public class CatalogoServlet extends JsonServlet {

    private ProductoService productoService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (usuarioSesion(req) == null) {
            enviarMensaje(resp, 401, "Debe iniciar sesion");
            return;
        }
        try {
            enviarJson(resp, 200, productoService.catalogoAros(
                    parametro(req, "marca"),
                    parametro(req, "material"),
                    parametro(req, "forma"),
                    parametroDecimal(req, "precioMin"),
                    parametroDecimal(req, "precioMax")));
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }
}
