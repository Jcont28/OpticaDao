IF DB_NAME() <> 'optica'
BEGIN
    RAISERROR('Seleccione la base de datos optica antes de ejecutar este script', 16, 1);
    SET NOEXEC ON;
END
GO

IF OBJECT_ID('Ventas') IS NOT NULL AND OBJECT_ID('Ventas_anterior') IS NULL
    EXEC sp_rename 'Ventas', 'Ventas_anterior';
IF OBJECT_ID('Apartados') IS NOT NULL AND OBJECT_ID('Apartados_anterior') IS NULL
    EXEC sp_rename 'Apartados', 'Apartados_anterior';
IF OBJECT_ID('Mediciones') IS NOT NULL AND OBJECT_ID('Mediciones_anterior') IS NULL
    EXEC sp_rename 'Mediciones', 'Mediciones_anterior';
IF OBJECT_ID('Productos') IS NOT NULL AND OBJECT_ID('Productos_anterior') IS NULL
    EXEC sp_rename 'Productos', 'Productos_anterior';
IF OBJECT_ID('Vendedores') IS NOT NULL AND OBJECT_ID('Vendedores_anterior') IS NULL
    EXEC sp_rename 'Vendedores', 'Vendedores_anterior';
IF OBJECT_ID('Clientes') IS NOT NULL AND OBJECT_ID('Clientes_anterior') IS NULL
    EXEC sp_rename 'Clientes', 'Clientes_anterior';
IF OBJECT_ID('Usuarios') IS NOT NULL AND OBJECT_ID('Usuarios_anterior') IS NULL
    EXEC sp_rename 'Usuarios', 'Usuarios_anterior';
GO

DROP TABLE IF EXISTS Ventas;
DROP TABLE IF EXISTS Apartados;
DROP TABLE IF EXISTS Mediciones;
DROP TABLE IF EXISTS Productos;
DROP TABLE IF EXISTS Vendedores;
DROP TABLE IF EXISTS Clientes;
DROP TABLE IF EXISTS Usuarios;
GO

CREATE TABLE Usuarios (
    UsuarioID INT IDENTITY(1,1) PRIMARY KEY,
    Usuario NVARCHAR(120) NOT NULL UNIQUE,
    PasswordHash NVARCHAR(100) NOT NULL,
    Rol NVARCHAR(20) NOT NULL CHECK (Rol IN ('CLIENTE', 'VENDEDOR')),
    Activo BIT NOT NULL DEFAULT 1
);

CREATE TABLE Clientes (
    ClienteID INT IDENTITY(1,1) PRIMARY KEY,
    UsuarioID INT NOT NULL UNIQUE REFERENCES Usuarios(UsuarioID),
    Nombre NVARCHAR(120) NOT NULL,
    DNI NVARCHAR(30) NOT NULL UNIQUE,
    Telefono NVARCHAR(30),
    Correo NVARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE Vendedores (
    VendedorID INT IDENTITY(1,1) PRIMARY KEY,
    UsuarioID INT NOT NULL UNIQUE REFERENCES Usuarios(UsuarioID),
    Nombre NVARCHAR(120) NOT NULL
);

CREATE TABLE Mediciones (
    MedicionID INT IDENTITY(1,1) PRIMARY KEY,
    ClienteID INT NOT NULL REFERENCES Clientes(ClienteID),
    Fecha DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    EsferaOD DECIMAL(5,2) NOT NULL,
    CilindroOD DECIMAL(5,2) NOT NULL DEFAULT 0,
    EjeOD INT NOT NULL DEFAULT 0 CHECK (EjeOD BETWEEN 0 AND 180),
    EsferaOI DECIMAL(5,2) NOT NULL,
    CilindroOI DECIMAL(5,2) NOT NULL DEFAULT 0,
    EjeOI INT NOT NULL DEFAULT 0 CHECK (EjeOI BETWEEN 0 AND 180),
    Adicion DECIMAL(4,2) NOT NULL DEFAULT 0,
    DistanciaPupilar DECIMAL(4,1) NOT NULL DEFAULT 0,
    Observaciones NVARCHAR(300)
);

CREATE TABLE Productos (
    ProductoID INT IDENTITY(1,1) PRIMARY KEY,
    Nombre NVARCHAR(120) NOT NULL,
    Tipo NVARCHAR(20) NOT NULL CHECK (Tipo IN ('ARO', 'LUNA', 'ACCESORIO')),
    Marca NVARCHAR(80),
    Material NVARCHAR(80),
    Forma NVARCHAR(80),
    Precio DECIMAL(10,2) NOT NULL CHECK (Precio >= 0),
    Stock INT NOT NULL CHECK (Stock >= 0),
    Activo BIT NOT NULL DEFAULT 1
);

CREATE TABLE Apartados (
    ApartadoID INT IDENTITY(1,1) PRIMARY KEY,
    ClienteID INT NOT NULL REFERENCES Clientes(ClienteID),
    AroID INT NOT NULL REFERENCES Productos(ProductoID),
    Fecha DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    Estado NVARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
);

CREATE TABLE Ventas (
    VentaID INT IDENTITY(1,1) PRIMARY KEY,
    ClienteID INT NOT NULL REFERENCES Clientes(ClienteID),
    MedicionID INT NOT NULL REFERENCES Mediciones(MedicionID),
    AroID INT NOT NULL REFERENCES Productos(ProductoID),
    LunaID INT NOT NULL REFERENCES Productos(ProductoID),
    VendedorID INT NOT NULL REFERENCES Vendedores(VendedorID),
    Cantidad INT NOT NULL CHECK (Cantidad > 0),
    PrecioAro DECIMAL(10,2) NOT NULL,
    PrecioLuna DECIMAL(10,2) NOT NULL,
    Subtotal DECIMAL(12,2) NOT NULL,
    Impuesto DECIMAL(12,2) NOT NULL,
    Total DECIMAL(12,2) NOT NULL,
    Fecha DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

SET NOEXEC OFF;
GO
