CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO roles (name) VALUES ('Repartidor');
INSERT INTO roles (name) VALUES ('Usuario');

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dni VARCHAR(8) NOT NULL UNIQUE,
    phone VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    active BOOLEAN,
    verification_code VARCHAR(64),
    verification_code_expiry TIMESTAMP,
    email_verified BOOLEAN,
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);


CREATE TABLE IF NOT EXISTS routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    package_info VARCHAR(255),
    origin VARCHAR(255),
    destination VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    user_id BIGINT,
    delivery_user_id BIGINT,
    created_at DATE NOT NULL,
    updated_at DATE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (delivery_user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS password_reset_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO routes (package_info, origin, destination, status, user_id, delivery_user_id)
VALUES
    ('Caja mediana', 'Calle Falsa 123', 'Avenida Central 456', 'pendiente', NULL, NULL),
    ('Sobre confidencial', 'Oficina 45, Edificio Sur', 'Oficina 10, Edificio Norte', 'pendiente', NULL, NULL),
    ('Paquete frágil', 'San Martín 300', 'Mitre 400', 'en camino', NULL, NULL),
    ('Electrodoméstico', 'Av. Libertador 700', 'Calle Belgrano 1500', 'pendiente', NULL, NULL),
    ('Documentos legales', 'Centro Cívico', 'Juzgado Federal', 'entregado', NULL, NULL),
    ('Regalo envuelto', 'Shopping Central', 'Barrio Jardines', 'pendiente', NULL, NULL),
    ('Medicamentos', 'Farmacia La Salud', 'Hospital General', 'en camino', NULL, NULL),
    ('Electrónica', 'Depósito Zona Oeste', 'Cliente Avellaneda', 'pendiente', NULL, NULL),
    ('Comestibles', 'Mercado de Abasto', 'Residencia Del Sol', 'pendiente', NULL, NULL),
    ('Ropa', 'Tienda Fashion', 'Sucursal Palermo', 'entregado', NULL, NULL);