package hn.optica.controller;

import com.google.gson.JsonObject;
import hn.optica.model.Usuario;
import hn.optica.service.UsuarioService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/login", "/api/logout", "/api/registro", "/api/sesion"})
public class AuthServlet extends JsonServlet {

    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Usuario u = usuarioSesion(req);
        if (u == null) {
            enviarMensaje(resp, 401, "Debe iniciar sesion");
            return;
        }
        enviarJson(resp, 200, datosUsuario(u));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ruta = req.getServletPath();
        try {
            if (ruta.equals("/api/login")) {
                login(req, resp);
            } else if (ruta.equals("/api/registro")) {
                registro(req, resp);
            } else {
                HttpSession sesion = req.getSession(false);
                if (sesion != null) {
                    sesion.invalidate();
                }
                enviarMensaje(resp, 200, "Sesion cerrada");
            }
        } catch (Exception e) {
            enviarError(resp, e);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        JsonObject datos = leerJson(req);
        Usuario u = usuarioService.login(texto(datos, "usuario"), texto(datos, "password"));
        HttpSession anterior = req.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }
        HttpSession sesion = req.getSession(true);
        sesion.setAttribute("usuario", u);
        enviarJson(resp, 200, datosUsuario(u));
    }

    private void registro(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        JsonObject datos = leerJson(req);
        usuarioService.registrarCliente(
                texto(datos, "nombre"),
                texto(datos, "dni"),
                texto(datos, "telefono"),
                texto(datos, "correo"),
                texto(datos, "password"));
        enviarMensaje(resp, 200, "Cuenta creada. Ya puedes iniciar sesion.");
    }

    private Map<String, Object> datosUsuario(Usuario u) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("usuario", u.getUsuario());
        datos.put("nombre", u.getNombre());
        datos.put("rol", u.getRol());
        if (UsuarioService.ROL_CLIENTE.equals(u.getRol())) {
            datos.put("destino", "catalogo.html");
        } else {
            datos.put("destino", "productos.html");
        }
        return datos;
    }
}
