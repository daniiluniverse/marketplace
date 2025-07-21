create schema if not exists products;

create table products.t_products(
    id serial primary key,
    name varchar not null,
    details varchar,
    price double precision
);