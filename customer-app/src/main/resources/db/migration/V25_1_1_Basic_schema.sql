create schema if not exists customers;

create table customers.t_favourite_products(

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id int not null
);
