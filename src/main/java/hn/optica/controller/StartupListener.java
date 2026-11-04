package hn.optica.controller;

import hn.optica.service.UsuarioService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        try {
            new UsuarioService().crearVendedorDemo();
        } catch (Exception e) {
            evento.getServletContext().log("No se pudo crear el vendedor demo. Revise la conexion a la base de datos", e);
        }
    }
}
