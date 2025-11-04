-- 1. CREAR LAS TABLAS

CREATE TABLE ciudades (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100)
);

CREATE TABLE ubicaciones (
    id SERIAL PRIMARY KEY,
    latitud DECIMAL(10, 6),
    longitud DECIMAL(10, 6),
    ciudad_id INT,
    direccion_txt VARCHAR(255)
);

CREATE TABLE depositos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE NOT NULL,
    ubicacion_id INT NOT NULL,
    costo_estadia_diario DECIMAL(10, 2) NOT NULL
);

CREATE TABLE camiones (
    dominio VARCHAR(20) PRIMARY KEY,
    transportista_legajo VARCHAR(100), -- FK Lógica a db_usuarios.Transportista
    capacidad_peso DECIMAL(10, 2) NOT NULL,
    capacidad_volumen DECIMAL(10, 2) NOT NULL,
    disponibilidad BOOLEAN DEFAULT true,
    consumo_combustible_promedio DECIMAL(5, 2) NOT NULL
);

CREATE TABLE tarifas_volumen (
    id SERIAL PRIMARY KEY,
    volumen_min DECIMAL(10, 2) NOT NULL,
    volumen_max DECIMAL(10, 2) NOT NULL,
    costo_km_base DECIMAL(10, 2) NOT NULL
    -- Note: 'Define el costo base por KM según el volumen'
);

CREATE TABLE tarifas_combustible (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    precio_litro DECIMAL(10, 2) NOT NULL
    -- Note: 'Almacena el precio de los combustibles'
);

-- 2. AÑADIR LAS LLAVES FORÁNEAS (FOREIGN KEYS)
-- Se añaden al final para evitar errores de orden de creación

ALTER TABLE ubicaciones
ADD FOREIGN KEY (ciudad_id) REFERENCES ciudades(id);

ALTER TABLE depositos
ADD FOREIGN KEY (ubicacion_id) REFERENCES ubicaciones(id);