-- LIMPIEZA 
DROP TABLE IF EXISTS loans, equipments, users, prestamos, equipos, usuarios CASCADE;

-- USUARIOS
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'USER')),
    
    -- Spring Security
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE
);

-- EQUIPOS
CREATE TABLE equipos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    tipo VARCHAR(50),
    marca VARCHAR(50),
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('DISPONIBLE', 'PRESTADO'))
);

-- PRESTAMOS
CREATE TABLE prestamos (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL,
    equipo_id INTEGER NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('ACTIVO', 'DEVUELTO')),
    
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_equipo FOREIGN KEY (equipo_id) REFERENCES equipos(id)
);

-- DATOS INICIALES (contrasena: password)
INSERT INTO usuarios (nombre, usuario, correo, contrasena, rol) VALUES 
('Admin Sistema', 'admin', 'admin@greentrack.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN'),
('Rodrigo Valverde', 'rodrigo', 'rodrigo@test.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER');

INSERT INTO equipos (nombre, tipo, marca, estado) VALUES 
('Macbook Pro M3', 'Laptop', 'Apple', 'DISPONIBLE'),
('Logitech MX Master 3S', 'Periferico', 'Logitech', 'DISPONIBLE');