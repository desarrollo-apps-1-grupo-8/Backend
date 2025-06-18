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
    user_id BIGINT NOT NULL,
    delivery_user_id BIGINT,
    created_at DATE NOT NULL,
    updated_at DATE,
    qr VARCHAR(255),
    qr_scanned BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (delivery_user_id) REFERENCES users(id)
);