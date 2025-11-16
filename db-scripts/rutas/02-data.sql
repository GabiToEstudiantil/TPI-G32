-- 1. Ciudades (10)
INSERT INTO ciudades (nombre) VALUES
('Córdoba'), ('Rosario'), ('Buenos Aires'), ('Mendoza'), ('Salta'),
('Bariloche'), ('Ushuaia'), ('Posadas'), ('Mar del Plata'), ('La Plata');

-- 2. Ubicaciones (10 de Clientes + 5 de Depósitos)
-- IDs 1-10: Clientes (Origenes/Destinos)
INSERT INTO ubicaciones (latitud, longitud, id_ciudad, direccion_textual) VALUES
(-31.4173, -64.1837, 1, 'Av. Colón 100, Córdoba, Argentina'), -- ID 1 (Cliente Cba)
(-34.6037, -58.3816, 3, 'Av. 9 de Julio 1000, Buenos Aires, Argentina'), -- ID 2 (Cliente BA)
(-32.8908, -68.8272, 4, 'Av. San Martín 800, Mendoza, Argentina'), -- ID 3 (Cliente Mendoza)
(-24.7821, -65.4232, 5, 'Av. Belgrano 50, Salta, Argentina'), -- ID 4 (Cliente Salta)
(-41.1335, -71.3103, 6, 'Av. Bustillo Km 1, Bariloche, Argentina'), -- ID 5 (Cliente Bariloche)
(-54.8019, -68.3030, 7, 'Maipú 100, Ushuaia, Argentina'), -- ID 6 (Cliente Ushuaia)
(-27.3671, -55.8961, 8, 'Bolívar 1500, Posadas, Argentina'), -- ID 7 (Cliente Posadas)
(-38.0055, -57.5426, 9, 'Av. Luro 3200, Mar del Plata, Argentina'), -- ID 8 (Cliente MdP)
(-34.9205, -57.9536, 10, 'Calle 7 800, La Plata, Argentina'), -- ID 9 (Cliente La Plata)
(-31.3184, -64.2378, 1, 'Av. La Voz del Interior 8000, Córdoba, Argentina'), -- ID 10 (Cliente Cba 2)

-- IDs 11-15: Depósitos (Paradas Intermedias)
(-32.9471, -60.6322, 2, 'Av. Pellegrini 500, Rosario, Argentina'), -- ID 11 (Depósito Rosario)
(-34.7554, -58.4947, 3, 'Autopista Ricchieri Km 20, Buenos Aires, Argentina'), -- ID 12 (Depósito BA Hub)
(-38.9517, -68.0592, 4, 'Ruta 22 Km 1200, Neuquén, Argentina'), -- ID 13 (Depósito Neuquén - Falla en ciudad, pero para ejemplo sirve)
(-26.8329, -65.2226, 5, 'Av. Circunvalación 100, Tucumán, Argentina'), -- ID 14 (Depósito Tucumán - Falla en ciudad)
(-33.0243, -60.5595, 2, 'Ruta 9 Km 200, San Nicolás, Argentina'); -- ID 15 (Depósito Intermedio Cba-BsAs)

-- 3. Depósitos (5)
INSERT INTO depositos (nombre, id_ubicacion, costo_estadia_diario) VALUES
('Deposito Rosario Central', 11, 25000.00),
('HUB Logístico Buenos Aires', 12, 45000.00),
('Cargas Patagonia (Neuquén)', 13, 30000.00),
('Logística Norte (Tucumán)', 14, 28000.00),
('Parada San Nicolás', 15, 20000.00);

-- 4. Tarifas de Combustible (3)
INSERT INTO tarifas_combustible (nombre, precio_litro) VALUES
('Infinia Diesel', 1150.50),
('Diesel Premium', 1080.00),
('Diesel Común', 980.00);

-- 5. Tarifas por Volumen (m3) (4)
INSERT INTO tarifas_volumen (volumen_min, volumen_max, costo_km_base) VALUES
(0.0, 10.0, 150.0),   -- Pequeño
(10.01, 30.0, 200.0), -- Mediano
(30.01, 60.0, 280.0), -- Grande
(60.01, 100.0, 350.0); -- Extra Grande

-- 6. Camiones (10)
INSERT INTO camiones (dominio, transportista_legajo, capacidad_peso, capacidad_volumen, disponibilidad, consumo_combustible_promedio) VALUES
('AA123BC', 'TR-101', 5000.0, 20.0, true, 0.25),
('AB456CD', 'TR-101', 12000.0, 50.0, true, 0.35),
('AC789DE', 'TR-102', 4000.0, 15.0, false, 0.22), -- No disponible
('AD012EF', 'TR-102', 15000.0, 60.0, true, 0.40),
('AE345FG', 'TR-103', 6000.0, 25.0, true, 0.28),
('AF678GH', 'TR-103', 10000.0, 40.0, true, 0.32),
('AG901IJ', 'TR-104', 8000.0, 35.0, true, 0.30),
('AH234KL', 'TR-104', 20000.0, 80.0, true, 0.45), -- Capacidad muy alta
('AI567MN', 'TR-105', 5000.0, 20.0, true, 0.26),
('AJ890OP', 'TR-105', 12000.0, 50.0, false, 0.36); -- No disponible