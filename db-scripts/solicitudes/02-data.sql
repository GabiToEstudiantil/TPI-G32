-- 1. INSERTAR CONTENEDORES (15 contenedores, algunos ya asignados)
INSERT INTO contenedores (codigo_identificacion, peso, volumen, estado, cliente_dni) VALUES
('CONT-001', 4500.00, 18.50, 'DISPONIBLE', '20111222'),
('CONT-002', 3200.00, 12.00, 'DISPONIBLE', '20111222'),
('CONT-003', 5800.00, 22.00, 'ASIGNADO', '25333444'),
('CONT-004', 4100.00, 16.00, 'ASIGNADO', '25333444'),
('CONT-005', 2900.00, 10.50, 'DISPONIBLE', '30555666'),
('CONT-006', 6200.00, 25.00, 'EN_TRANSITO', '28123456'),
('CONT-007', 3800.00, 14.50, 'DISPONIBLE', '32777888'),
('CONT-008', 5100.00, 19.00, 'ASIGNADO', '22999000'),
('CONT-009', 4700.00, 17.50, 'EN_DEPOSITO', '34111222'),
('CONT-010', 3500.00, 13.00, 'DISPONIBLE', '27555444'),
('CONT-011', 6800.00, 28.00, 'DISPONIBLE', '31888999'),
('CONT-012', 4300.00, 15.50, 'ASIGNADO', '26444333'),
('CONT-013', 5500.00, 21.00, 'DISPONIBLE', '20111222'),
('CONT-014', 3900.00, 14.00, 'DISPONIBLE', '25333444'),
('CONT-015', 7200.00, 30.00, 'DISPONIBLE', '30555666');

-- 2. INSERTAR SOLICITUDES (5 solicitudes en diferentes estados)
-- Nota: Una solicitud tiene UN SOLO contenedor según el esquema (contenedor_id)
INSERT INTO solicitudes (fecha_creacion, costo_estimado, tiempo_estimado, costo_final, tiempo_real, cliente_dni, contenedor_id) VALUES
-- Solicitud 1: Córdoba -> Buenos Aires (COMPLETADA)
(NOW() - INTERVAL '7 days', 18500.00, '12 horas 30 minutos', 19200.00, '13 horas 15 minutos', '25333444', 3),

-- Solicitud 2: Buenos Aires -> Mendoza (EN CURSO)
(NOW() - INTERVAL '2 days', 22000.00, '15 horas', NULL, NULL, '22999000', 8),

-- Solicitud 3: Salta -> Bariloche (COMPLETADA)
(NOW() - INTERVAL '10 days', 35000.00, '28 horas', 36500.00, '29 horas 45 minutos', '28123456', 6),

-- Solicitud 4: Posadas -> Buenos Aires (PENDIENTE - recién creada)
(NOW() - INTERVAL '1 hour', 28000.00, '18 horas', NULL, NULL, '34111222', 9),

-- Solicitud 5: Córdoba -> Rosario (COMPLETADA)
(NOW() - INTERVAL '15 days', 8500.00, '4 horas 30 minutos', 8200.00, '4 horas 15 minutos', '26444333', 12);

-- 3. INSERTAR RUTAS (una por solicitud)
INSERT INTO rutas (cantidad_tramos, cantidad_depositos, solicitud_id) VALUES
(2, 1, 1),  -- Ruta Solicitud 1: origen -> depósito -> destino
(3, 2, 2),  -- Ruta Solicitud 2: origen -> depósito -> depósito -> destino
(3, 2, 3),  -- Ruta Solicitud 3: origen -> depósito -> depósito -> destino
(2, 1, 4),  -- Ruta Solicitud 4: origen -> depósito -> destino
(1, 0, 5);  -- Ruta Solicitud 5: origen -> destino (directo, sin depósitos)

-- 4. INSERTAR TRAMOS
-- Nota: origen_id y destino_id son IDs de ubicaciones del microservicio de rutas
-- Los IDs 1-10 son clientes (origenes/destinos), 11-15 son depósitos

-- Ruta 1 (Solicitud 1: Córdoba -> BA)
INSERT INTO tramos (origen_id, destino_id, tipo, estado, distancia_km, costo_aproximado, costo_real, fecha_hora_inicio, fecha_hora_fin, ruta_id, camion_dominio, orden_en_ruta) VALUES
(1, 11, 'ORIGEN_DEPOSITO', 'FINALIZADO', 320.00, 6400.00, 6500.00, NOW() - INTERVAL '7 days', NOW() - INTERVAL '6 days 18 hours', 1, 'AB456CD', 1),
(11, 3, 'DEPOSITO_DESTINO', 'FINALIZADO', 310.00, 12100.00, 12700.00, NOW() - INTERVAL '6 days 16 hours', NOW() - INTERVAL '6 days 5 hours', 1, 'AB456CD', 2);

-- Ruta 2 (Solicitud 2: BA -> Mendoza) - EN CURSO
INSERT INTO tramos (origen_id, destino_id, tipo, estado, distancia_km, costo_aproximado, costo_real, fecha_hora_inicio, fecha_hora_fin, ruta_id, camion_dominio, orden_en_ruta) VALUES
(3, 15, 'ORIGEN_DEPOSITO', 'FINALIZADO', 180.00, 4500.00, 4600.00, NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day 20 hours', 2, 'AF678GH', 1),
(15, 11, 'DEPOSITO_DEPOSITO', 'FINALIZADO', 140.00, 3800.00, 3850.00, NOW() - INTERVAL '1 day 18 hours', NOW() - INTERVAL '1 day 14 hours', 2, 'AF678GH', 2),
(11, 4, 'DEPOSITO_DESTINO', 'INICIADO', 720.00, 13700.00, NULL, NOW() - INTERVAL '1 day 12 hours', NULL, 2, 'AF678GH', 3);

-- Ruta 3 (Solicitud 3: Salta -> Bariloche) - COMPLETADA
INSERT INTO tramos (origen_id, destino_id, tipo, estado, distancia_km, costo_aproximado, costo_real, fecha_hora_inicio, fecha_hora_fin, ruta_id, camion_dominio, orden_en_ruta) VALUES
(5, 14, 'ORIGEN_DEPOSITO', 'FINALIZADO', 280.00, 7000.00, 7200.00, NOW() - INTERVAL '10 days', NOW() - INTERVAL '9 days 20 hours', 3, 'AD012EF', 1),
(14, 13, 'DEPOSITO_DEPOSITO', 'FINALIZADO', 950.00, 19000.00, 19500.00, NOW() - INTERVAL '9 days 18 hours', NOW() - INTERVAL '9 days 6 hours', 3, 'AD012EF', 2),
(13, 6, 'DEPOSITO_DESTINO', 'FINALIZADO', 480.00, 9000.00, 9800.00, NOW() - INTERVAL '9 days 4 hours', NOW() - INTERVAL '8 days 18 hours', 3, 'AD012EF', 3);

-- Ruta 4 (Solicitud 4: Posadas -> BA) - PENDIENTE
INSERT INTO tramos (origen_id, destino_id, tipo, estado, distancia_km, costo_aproximado, costo_real, fecha_hora_inicio, fecha_hora_fin, ruta_id, camion_dominio, orden_en_ruta) VALUES
(8, 11, 'ORIGEN_DEPOSITO', 'ESTIMADO', 680.00, 13600.00, NULL, NULL, NULL, 4, 'AI567MN', 1),
(11, 3, 'DEPOSITO_DESTINO', 'ESTIMADO', 310.00, 14400.00, NULL, NULL, NULL, 4, 'AI567MN', 2);

-- Ruta 5 (Solicitud 5: Córdoba -> Rosario) - COMPLETADA, SIN DEPÓSITOS
INSERT INTO tramos (origen_id, destino_id, tipo, estado, distancia_km, costo_aproximado, costo_real, fecha_hora_inicio, fecha_hora_fin, ruta_id, camion_dominio, orden_en_ruta) VALUES
(1, 2, 'ORIGEN_DESTINO', 'FINALIZADO', 320.00, 8500.00, 8200.00, NOW() - INTERVAL '15 days', NOW() - INTERVAL '14 days 20 hours', 5, 'AA123BC', 1);

-- 5. INSERTAR PARADAS EN DEPÓSITOS (solo para rutas con depósitos intermedios)
-- Nota: deposito_id referencia IDs de ubicaciones que son depósitos (11-15)

-- Ruta 1: Parada en depósito Rosario (id=11)
INSERT INTO paradas_en_deposito (fecha_hora_llegada, fecha_hora_salida, segundos_estadia, costo_total_estadia, orden_en_ruta, ruta_id, deposito_id) VALUES
(NOW() - INTERVAL '6 days 18 hours', NOW() - INTERVAL '6 days 16 hours', 7200, 25000.00, 1, 1, 11);

-- Ruta 2: Paradas en depósito San Nicolás (id=15) y Rosario (id=11)
INSERT INTO paradas_en_deposito (fecha_hora_llegada, fecha_hora_salida, segundos_estadia, costo_total_estadia, orden_en_ruta, ruta_id, deposito_id) VALUES
(NOW() - INTERVAL '1 day 20 hours', NOW() - INTERVAL '1 day 18 hours', 7200, 20000.00, 1, 2, 15),
(NOW() - INTERVAL '1 day 14 hours', NOW() - INTERVAL '1 day 12 hours', 7200, 25000.00, 2, 2, 11);

-- Ruta 3: Paradas en depósito Tucumán (id=14) y Neuquén (id=13)
INSERT INTO paradas_en_deposito (fecha_hora_llegada, fecha_hora_salida, segundos_estadia, costo_total_estadia, orden_en_ruta, ruta_id, deposito_id) VALUES
(NOW() - INTERVAL '9 days 20 hours', NOW() - INTERVAL '9 days 18 hours', 7200, 28000.00, 1, 3, 14),
(NOW() - INTERVAL '9 days 6 hours', NOW() - INTERVAL '9 days 4 hours', 7200, 30000.00, 2, 3, 13);

-- Ruta 4: Parada planificada en depósito Rosario (id=11) - aún no llegó
INSERT INTO paradas_en_deposito (fecha_hora_llegada, fecha_hora_salida, segundos_estadia, costo_total_estadia, orden_en_ruta, ruta_id, deposito_id) VALUES
(NULL, NULL, NULL, NULL, 1, 4, 11);