CREATE SCHEMA IF NOT EXISTS products;

CREATE TABLE IF NOT EXISTS products.t_products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    details TEXT,
    price DECIMAL(10,2) NOT NULL
    );


insert into products.t_products (id, name, details, price)
values (1,'Товар 1', 'Описание товара 1', 1000 ),
       (3,'Товар 3', 'Описание товара 3', 2000 );