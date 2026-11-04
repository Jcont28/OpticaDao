package hn.optica.controller;

import com.google.gson.JsonObject;
import hn.optica.model.Producto;
import hn.optica.service.ProductoService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/productos", "/api/alertas"})
public class ProductoServlet extends JsonServlet {

    private ProductoService productoService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!permitirVendedor(req, resp)) {
            return;
        }
        try {
            if (req.getServletPath().equals("/api/alertas")) {
                enviarJson(resp, 200, productoService.arosConStockBajo());
                return;
            }
            enviarJson(resp, 200, productoService.listar(
                    parametro(req, "tipo"),
                    parametro(req, "marca"),
                    parametro(req, "material"),
                    parametro(req, "forma"),
                    parametroDecimal(req, "precioMin"),
                    parametroDecimal(req, "precioMax")));
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
            Producto p = leerProducto(leerJson(req));
            productoService.guardar(p);
            enviarMensaje(resp, 200, "Producto guardado");
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!permitirVendedor(req, resp)) {
            return;
        }
        try {
            JsonObject datos = leerJson(req);
            Producto p = leerProducto(datos);
            p.setId(entero(datos, "id"));
            productoService.actualizar(p);
            enviarMensaje(resp, 200, "Producto actualizado");
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!permitirVendedor(req, resp)) {
            return;
        }
        try {
            productoService.eliminar(parametroEntero(req, "id"));
            enviarMensaje(resp, 200, "Producto eliminado");
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }

    private Producto leerProducto(JsonObject datos) {
        Producto p = new Producto();
        p.setNombre(texto(datos, "nombre"));
        p.setTipo(texto(datos, "tipo").toUpperCase());
        p.setMarca(texto(datos, "marca"));
        p.setMaterial(texto(datos, "material"));
        p.setForma(texto(datos, "forma"));
        p.setPrecio(decimal(datos, "precio"));
        p.setStock(entero(datos, "stock"));
        return p;
    }
}
