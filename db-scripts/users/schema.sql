-- 1. CREAR LAS TABLAS

CREATE TABLE clientes (
    dni VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefono VARCHAR(50),
    keycloak_id VARCHAR(100) UNIQUE
    -- Note: 'Almacena la información de los clientes'
);

CREATE TABLE transportistas (
    legajo VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    dni VARCHAR(100),
    email VARCHAR(100),
    telefono VARCHAR(100),
    keycloak_id VARCHAR(100) UNIQUE
    -- Note: 'Almacena la información de los transportistas'
);