create schema if not exists orders;



CREATE TABLE IF NOT EXISTS orders.t_orders(
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    order_status VARCHAR(20) NOT NULL DEFAULT 'CREATED',
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    address VARCHAR(500),
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



create table orders.t_order_items(
                                                                             id BIGSERIAL PRIMARY KEY,
                                                                             order_id BIGINT NOT NULL,
                                                                             product_id BIGINT NOT NULL,
                                                                             product_name VARCHAR(255) NOT NULL,
                                                                             price DECIMAL(10,2) NOT NULL,
                                                                             quantity INTEGER NOT NULL DEFAULT 1
                                        );
