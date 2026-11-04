package hn.optica.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hn.optica.model.Usuario;
import hn.optica.service.UsuarioService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonServlet extends HttpServlet {

    protected Gson gson = new Gson();

    protected JsonObject leerJson(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            JsonElement elemento = JsonParser.parseReader(req.getReader());
            if (elemento != null && elemento.isJsonObject()) {
                return elemento.getAsJsonObject();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Los datos enviados no son validos");
        }
        return new JsonObject();
    }

    protected void enviarJson(HttpServletResponse resp, int estado, Object datos) throws IOException {
        resp.setStatus(estado);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        resp.getWriter().write(gson.toJson(datos));
    }

    protected void enviarMensaje(HttpServletResponse resp, int estado, String mensaje) throws IOException {
        Map<String, Object> datos = new HashMap<>();
        datos.put("mensaje", mensaje);
        enviarJson(resp, estado, datos);
    }

    protected void enviarError(HttpServletResponse resp, Exception e) throws IOException {
        if (e instanceof IllegalArgumentException) {
            enviarMensaje(resp, 400, e.getMessage());
        } else {
            e.printStackTrace();
            enviarMensaje(resp, 500, "Error en el servidor: " + e.getMessage());
        }
    }

    protected Usuario usuarioSesion(HttpServletRequest req) {
        HttpSession sesion = req.getSession(false);
        if (sesion == null) {
            return null;
        }
        return (Usuario) sesion.getAttribute("usuario");
    }

    protected boolean esVendedor(HttpServletRequest req) {
        Usuario u = usuarioSesion(req);
        return u != null && UsuarioService.ROL_VENDEDOR.equals(u.getRol());
    }

    protected boolean esCliente(HttpServletRequest req) {
        Usuario u = usuarioSesion(req);
        return u != null && UsuarioService.ROL_CLIENTE.equals(u.getRol());
    }

    protected boolean permitirVendedor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (usuarioSesion(req) == null) {
            enviarMensaje(resp, 401, "Debe iniciar sesion");
            return false;
        }
        if (!esVendedor(req)) {
            enviarMensaje(resp, 403, "Solo los vendedores pueden usar esta opcion");
            return false;
        }
        return true;
    }

    protected boolean permitirCliente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (usuarioSesion(req) == null) {
            enviarMensaje(resp, 401, "Debe iniciar sesion");
            return false;
        }
        if (!esCliente(req)) {
            enviarMensaje(resp, 403, "Solo los clientes pueden usar esta opcion");
            return false;
        }
        return true;
    }

    protected String texto(JsonObject datos, String campo) {
        if (datos.has(campo) && !datos.get(campo).isJsonNull()) {
            return datos.get(campo).getAsString().trim();
        }
        return "";
    }

    protected int entero(JsonObject datos, String campo) {
        String valor = texto(datos, campo);
        if (valor.isEmpty()) {
            return 0;
        }
        try {
            return (int) Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un numero");
        }
    }

    protected double decimal(JsonObject datos, String campo) {
        String valor = texto(datos, campo);
        if (valor.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un numero");
        }
    }

    protected String parametro(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        if (valor == null) {
            return "";
        }
        return valor.trim();
    }

    protected Double parametroDecimal(HttpServletRequest req, String nombre) {
        String valor = parametro(req, nombre);
        if (valor.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El filtro " + nombre + " debe ser un numero");
        }
    }

    protected int parametroEntero(HttpServletRequest req, String nombre) {
        String valor = parametro(req, nombre);
        if (valor.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El parametro " + nombre + " debe ser un numero");
        }
    }
}
