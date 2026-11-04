# Sistema de Óptica – Java + DAO

Aplicación web en Java (Servlets + JDBC) para gestionar una óptica, usando el patrón DAO y SQL Server en Azure.

## Despliegue

| Parte | Plataforma | Enlace |
|---|---|---|
| Páginas web (HTML/JS) | Vercel | https://opticadao.vercel.app |
| Backend Java (Tomcat) | Render (Docker, plan gratis) | https://opticadao.onrender.com |
| Base de datos | Azure SQL (`progav2.database.windows.net`, base `optica`) | |
| Código | GitHub | https://github.com/Jcont28/OpticaDao |

Vercel no ejecuta Java, por eso Vercel publica las páginas y reenvía todo lo que empieza con `/api/` al backend de Render (ver `vercel.json`). Render compila el proyecto con Maven y lo ejecuta en Tomcat 10.1 (ver `Dockerfile`).

## Usuarios de prueba

| Rol | Usuario | Contraseña |
|---|---|---|
| Vendedor | vendedor@optica.local | Admin123* |
| Cliente | cliente@optica.local | Cliente123* |

## Estructura (patrón DAO)

- `model`: Usuario, Cliente, Vendedor, Medicion, Producto, Aro, Apartado, Venta.
- `dao`: interfaces UsuarioDAO, ClienteDAO, VendedorDAO, MedicionDAO, ProductoDAO, AroDAO, ApartadoDAO, VentaDAO.
- `dao.jdbc`: implementaciones con JDBC.
- `service`: reglas de negocio (login con BCrypt, validaciones, cálculo de subtotal, impuesto y total, alertas de stock).
- `controller`: Servlets que reciben las peticiones y responden JSON.
- `config`: conexión a la base de datos.
- `src/main/webapp`: páginas HTML.

## Funcionalidades

- Login con redirección según el rol (Cliente o Vendedor).
- Registro de clientes (nombre, DNI, teléfono, correo).
- Cliente: historial de mediciones, catálogo de aros con filtros por marca, material, forma y precio, apartar aros.
- Vendedor: CRUD de aros, lunas y accesorios, alerta de aros con menos de 5 unidades, registro de mediciones, registro de ventas con cliente, medición, aro y luna, cálculo automático de subtotal, ISV y total, descuento automático del stock e historial de ventas.

## Base de datos

1. Abrir SQL Server Management Studio conectado a `progav2.database.windows.net`.
2. Seleccionar la base `optica`.
3. Ejecutar `database/optica.sql` y después `database/datos_iniciales_ejemplo.sql`.

## Ejecutar en NetBeans

1. Copiar `src/main/resources/db.properties.example` como `db.properties` y poner usuario y contraseña.
2. Configurar Tomcat 10.1 o superior con JDK 17.
3. Clean and Build y Run.

## Desplegar en Render

1. En Render: New → Blueprint, elegir el repositorio. Render lee `render.yaml`.
2. Llenar las variables:
   - `OPTICA_DB_URL` = `jdbc:sqlserver://progav2.database.windows.net:1433;database=optica;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30`
   - `OPTICA_DB_USER` y `OPTICA_DB_PASSWORD`.
3. En Azure, en el servidor `progav2` → Redes, permitir el acceso desde las IP de salida de Render.
4. Si la URL de Render no es `https://opticadao.onrender.com`, cambiarla en `vercel.json`.

El plan gratis de Render se apaga después de 15 minutos sin uso; la primera petición puede tardar cerca de un minuto.
