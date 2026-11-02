USE OpticaDB;
GO
-- El usuario vendedor demo se crea automáticamente al desplegar la aplicación:
-- vendedor@optica.local / Admin123*
-- Registra clientes desde login.html. Después agrega mediciones de prueba así:
-- Reemplaza el DNI por uno existente.
-- INSERT Mediciones(ClienteID,Ojo,Esfera,Cilindro,Eje,Adicion,Observaciones)
-- SELECT ClienteID,'OD',-1.25,-0.50,90,0,'Receta de ejemplo'
-- FROM Clientes WHERE DNI='0801199900000';
-- Registra al menos un producto ARO y uno LUNA desde el inventario.
