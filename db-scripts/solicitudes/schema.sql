-- 1. CREAR LOS TIPOS ENUM
-- Nombres en snake_case
CREATE TYPE tramo_tipo AS ENUM (
    'ORIGEN_DESTINO',
    'ORIGEN_DEPOSITO',
    'DEPOSITO_DEPOSITO',
    'DEPOSITO_DESTINO'
);

CREATE TYPE tramo_estado AS ENUM (
    'ESTIMADO',
    'ASIGNADO',
    'INICIADO',
    'FINALIZADO',
    'CANCELADO'
);

CREATE TYPE contenedor_estado AS ENUM (
    'DISPONIBLE',
    'ASIGNADO',
    'EN_TRANSITO',
    'EN_DEPOSITO'
);
-- 2. CREAR LAS TABLAS
-- Nombres de tabla en plural y snake_case
-- Nombres de columna en snake_case

CREATE TABLE contenedores (
    id SERIAL PRIMARY KEY,
    codigo_identificacion VARCHAR(100) UNIQUE NOT NULL,
    peso DECIMAL(10, 2) NOT NULL,
    volumen DECIMAL(10, 2) NOT NULL,
    estado contenedor_estado DEFAULT 'DISPONIBLE',
    cliente_dni VARCHAR(100) NOT NULL -- FK Lógica a db_usuarios.Cliente
);

CREATE TABLE solicitudes (
    id SERIAL PRIMARY KEY,
    fecha_creacion TIMESTAMP DEFAULT now(),
    costo_estimado DECIMAL(12, 2),
    tiempo_estimado VARCHAR(100),
    costo_final DECIMAL(12, 2),
    tiempo_real VARCHAR(100),
    cliente_dni VARCHAR(100) NOT NULL, -- FK Lógica a db_usuarios.Cliente
    contenedor_id INT UNIQUE NOT NULL,
    
    FOREIGN KEY (contenedor_id) REFERENCES contenedores(id)
);

CREATE TABLE rutas (
    id SERIAL PRIMARY KEY,
    cantidad_tramos INT,
    cantidad_depositos INT,
    solicitud_id INT UNIQUE NOT NULL,
    
    FOREIGN KEY (solicitud_id) REFERENCES solicitudes(id)
);

CREATE TABLE tramos (
    id SERIAL PRIMARY KEY,
    origen_id INT NOT NULL, -- FK Lógica a db_flota.Ubicacion
    destino_id INT NOT NULL, -- FK Lógica a db_flota.Ubicacion
    tipo tramo_tipo NOT NULL,
    estado tramo_estado NOT NULL DEFAULT 'ESTIMADO',
    distancia_km DECIMAL(10, 2),
    costo_aproximado DECIMAL(10, 2),
    costo_real DECIMAL(10, 2),
    fecha_hora_inicio TIMESTAMP,
    fecha_hora_fin TIMESTAMP,
    ruta_id INT NOT NULL,
    camion_dominio VARCHAR(20), -- FK Lógica a db_flota.Camion
    orden_en_ruta INT NOT NULL,
    
    FOREIGN KEY (ruta_id) REFERENCES rutas(id)
);

CREATE TABLE paradas_en_deposito (
    id SERIAL PRIMARY KEY,
    fecha_hora_llegada TIMESTAMP NOT NULL,
    fecha_hora_salida TIMESTAMP,
    segundos_estadia BIGINT,
    costo_total_estadia DECIMAL(12, 2),
    orden_en_ruta INT NOT NULL,
    ruta_id INT NOT NULL,
    deposito_id INT NOT NULL, -- FK Lógica a db_flota.Deposito

    FOREIGN KEY (ruta_id) REFERENCES rutas(id)
);