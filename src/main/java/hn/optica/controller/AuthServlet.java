package hn.optica.controller;

import hn.optica.service.OpticaService;
import hn.optica.model.Usuario; 
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.google.gson.JsonObject;
import org.mindrot.jbcrypt.BCrypt; 
import java.sql.*; 
import hn.optica.config.DB;

@WebServlet(urlPatterns={"/api/login","/api/logout","/api/registro"})
public class AuthServlet extends JsonServlet {
    private final OpticaService s = new OpticaService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws java.io.IOException {
        try {
            String path = req.getServletPath();
            JsonObject b = body(req);

            if (path.endsWith("login")) {
                Usuario u = s.usuarios.autenticar(str(b, "usuario"), str(b, "password"));
                if (u == null) {
                    res.setStatus(401);
                    json(res, java.util.Map.of("mensaje", "Usuario o contraseña incorrectos"));
                    return;
                }

                HttpSession session = req.getSession(true);
                session.setAttribute("UsuarioID", u.id);
                session.setAttribute("Nombre_Usuario", u.Nombre_Usuario);
                session.setAttribute("usuario", u.usuario);

                // Determinar la vista según el rol/tipo del usuario
                String destino = "CLIENTE".equalsIgnoreCase(u.rol) ? "catalogo.html" : "productos.html";

                json(res, java.util.Map.of(
                    "Nombre_Usuario", u.Nombre_Usuario,
                    "destino", destino
                ));
            } 
            else if (path.endsWith("registro")) {
                String pass = str(b, "password");
                if (pass == null || pass.length() < 8) {
                    throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
                }
                s.usuarios.crearCliente(
                    str(b, "nombre"),
                    str(b, "dni"),
                    str(b, "telefono"),
                    str(b, "correo"),
                    BCrypt.hashpw(pass, BCrypt.gensalt())
                );
                json(res, java.util.Map.of("mensaje", "Cuenta creada. Ya puedes iniciar sesión."));
            } 
            else {
                req.getSession().invalidate();
                json(res, java.util.Map.of("mensaje", "Sesión cerrada"));
            }
        } catch (Exception e) {
            res.setStatus(400);
            json(res, java.util.Map.of("mensaje", e.getMessage() == null ? "Error" : e.getMessage()));
        }
    }
}

