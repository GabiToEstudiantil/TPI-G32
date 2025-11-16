-- Insertar Clientes (10)
INSERT INTO clientes (dni, nombre, apellido, email, telefono, keycloak_id) VALUES
('20111222', 'Ana', 'García', 'ana.garcia@gmail.com', '351-123456', 'uuid-cliente-1'),
('25333444', 'Carlos', 'Martinez', 'carlos.martinez@hotmail.com', '351-654321', 'uuid-cliente-2'),
('30555666', 'Maria', 'Rodriguez', 'maria.r@outlook.com', '011-987654', 'uuid-cliente-3'),
('28123456', 'Javier', 'Lopez', 'javi.lopez@gmail.com', '351-112233', 'uuid-cliente-4'),
('32777888', 'Lucia', 'Fernandez', 'lucia.f@yahoo.com', '261-456789', 'uuid-cliente-5'),
('22999000', 'Miguel', 'Sanchez', 'miguel.s@gmail.com', '11-5555-4444', 'uuid-cliente-6'),
('34111222', 'Sofia', 'Gomez', 'sofi.gomez@hotmail.com', '341-333221', 'uuid-cliente-7'),
('27555444', 'Diego', 'Diaz', 'diego.diaz@gmail.com', '351-999888', 'uuid-cliente-8'),
('31888999', 'Valeria', 'Paz', 'vale.paz@outlook.com', '221-777665', 'uuid-cliente-9'),
('26444333', 'Martin', 'Alvarez', 'martin.a@gmail.com', '387-111222', 'uuid-cliente-10');

-- Insertar Transportistas (5)
INSERT INTO transportistas (legajo, nombre, apellido, dni, email, telefono, keycloak_id) VALUES
('TR-101', 'Juan', 'Perez', '18000111', 'juan.perez@transporte.com', '351-111000', 'uuid-trans-1'),
('TR-102', 'Luis', 'Gonzalez', '19222333', 'luis.gonzalez@cargas.com', '351-222000', 'uuid-trans-2'),
('TR-103', 'Elena', 'Ramirez', '21444555', 'elena.r@logistica.com', '11-333111', 'uuid-trans-3'),
('TR-104', 'Marcos', 'Silva', '20555666', 'marcos.silva@fletes.com', '341-444555', 'uuid-trans-4'),
('TR-105', 'Beatriz', 'Molina', '23666777', 'beatriz.m@distrib.com', '261-555666', 'uuid-trans-5');