CREATE SCHEMA IF NOT EXISTS products;

CREATE TABLE IF NOT EXISTS products.t_products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    details TEXT,
    price DECIMAL(10,2) NOT NULL
    );