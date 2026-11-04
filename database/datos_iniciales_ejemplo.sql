IF DB_NAME() <> 'optica'
BEGIN
    RAISERROR('Seleccione la base de datos optica antes de ejecutar este script', 16, 1);
    SET NOEXEC ON;
END
GO

INSERT INTO Usuarios (Usuario, PasswordHash, Rol) VALUES
('vendedor@optica.local', '$2a$10$9UOhHRMLQn4RFDROR4Hdg.7kK7/vMiFwcpRE9hwOjFwAHCnWISXga', 'VENDEDOR'),
('cliente@optica.local', '$2a$10$5H.UCR1JZBDnCwYKNjGcce4f9ihV72TJAC0WcCCgC6LhJYho1jxle', 'CLIENTE');

INSERT INTO Vendedores (UsuarioID, Nombre)
SELECT UsuarioID, 'Vendedor Demo' FROM Usuarios WHERE Usuario = 'vendedor@optica.local';

INSERT INTO Clientes (UsuarioID, Nombre, DNI, Telefono, Correo)
SELECT UsuarioID, 'Cliente Demo', '0801199900001', '9999-0000', 'cliente@optica.local' FROM Usuarios WHERE Usuario = 'cliente@optica.local';

INSERT INTO Mediciones (ClienteID, Fecha, EsferaOD, CilindroOD, EjeOD, EsferaOI, CilindroOI, EjeOI, Adicion, DistanciaPupilar, Observaciones)
SELECT ClienteID, '2026-05-10', -1.25, -0.50, 90, -1.00, -0.75, 85, 0, 62, 'Miopia leve' FROM Clientes WHERE DNI = '0801199900001';

INSERT INTO Mediciones (ClienteID, Fecha, EsferaOD, CilindroOD, EjeOD, EsferaOI, CilindroOI, EjeOI, Adicion, DistanciaPupilar, Observaciones)
SELECT ClienteID, '2026-10-20', -1.50, -0.50, 90, -1.25, -0.75, 80, 0, 62, 'Control anual' FROM Clientes WHERE DNI = '0801199900001';

INSERT INTO Productos (Nombre, Tipo, Marca, Material, Forma, Precio, Stock) VALUES
('Ray-Ban RB5154 Clubmaster', 'ARO', 'Ray-Ban', 'Acetato', 'Clubmaster', 3200.00, 8),
('Oakley Pitchman', 'ARO', 'Oakley', 'Metal', 'Rectangular', 2800.00, 3),
('Vogue VO5286', 'ARO', 'Vogue', 'Acetato', 'Ovalado', 1950.00, 12),
('Guess GU2700', 'ARO', 'Guess', 'Metal', 'Redondo', 1700.00, 4),
('Tommy Hilfiger TH1772', 'ARO', 'Tommy Hilfiger', 'TR90', 'Cuadrado', 2100.00, 6),
('Luna monofocal CR-39', 'LUNA', 'Essilor', 'CR-39', NULL, 850.00, 30),
('Luna policarbonato antirreflejo', 'LUNA', 'Hoya', 'Policarbonato', NULL, 1400.00, 20),
('Luna progresiva Varilux', 'LUNA', 'Essilor', 'Alto indice', NULL, 4500.00, 10),
('Estuche rigido', 'ACCESORIO', 'Generico', 'Plastico', NULL, 150.00, 40),
('Liquido limpiador 60 ml', 'ACCESORIO', 'Zeiss', NULL, NULL, 120.00, 25);
GO

SET NOEXEC OFF;
GO
