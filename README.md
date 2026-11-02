# Sistema de Óptica – Java + DAO (NetBeans)

Proyecto Maven WAR para abrir directamente en Apache NetBeans. Está planteado como aplicación web Java (Servlets/JDBC), conserva vistas HTML/Bootstrap y sustituye el servidor Node.js por controladores Java.

## Requisitos
- JDK 17 o superior
- Apache NetBeans con soporte Maven
- Apache Tomcat 10.1+ (Jakarta Servlet 6)
- SQL Server (la configuración incluida apunta a `localhost:1433`)

## Abrir y ejecutar
1. Extrae el ZIP.
2. En NetBeans: **File > Open Project** y selecciona la carpeta `OpticaDAO-NetBeans`.
3. Configura Tomcat 10.1+ en **Tools > Servers**.
4. Ejecuta `database/optica.sql` en SQL Server Management Studio.
5. Configura las variables de entorno `OPTICA_DB_URL`, `OPTICA_DB_USER`, `OPTICA_DB_PASSWORD`. Ejemplo URL: `jdbc:sqlserver://localhost:1433;databaseName=OpticaDB;encrypt=true;trustServerCertificate=true`.
6. Compila con **Clean and Build** y ejecuta sobre Tomcat.
7. Abre `http://localhost:8080/OpticaDAO/`.

## Usuario inicial
Al desplegar, la aplicación crea un vendedor de demostración si no existe: `vendedor@optica.local` / `Admin123*` (se guarda con BCrypt); cámbiala inmediatamente en un entorno real.

## Arquitectura
- `model`: entidades del negocio.
- `dao`: contratos DAO.
- `dao.jdbc`: implementaciones JDBC.
- `service`: reglas de negocio, validación y transacciones.
- `controller`: Servlets HTTP que reciben peticiones y responden JSON.
- `src/main/webapp`: HTML y JavaScript del navegador.

## Funcionalidad incluida
- Login y redirección por rol Cliente/Vendedor.
- Registro de cliente.
- Catálogo de aros con filtros y opción de apartado.
- Historial de mediciones por cliente.
- CRUD básico de inventario de aros, lunas y accesorios.
- Alertas de stock menor a 5.
- Registro de ventas con cliente, medición, aro y luna obligatorios; cálculo y descuento transaccional de stock.
- Historial de ventas.

## Notas
El sistema usa SQL Server porque el proyecto original estaba conectado a SQL Server. Las páginas son HTML y el backend es Java; JavaScript solo consume los endpoints de los Servlets. No se incluye `node_modules`, `.git` ni código Node.js. Las contraseñas nuevas deben guardarse con BCrypt. El primer usuario demo se crea con BCrypt al iniciar la aplicación si no existe.
