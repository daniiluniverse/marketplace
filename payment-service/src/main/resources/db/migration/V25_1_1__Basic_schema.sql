CREATE SCHEMA IF NOT EXISTS payments;

CREATE TABLE payments.t_payments (
                                     id BIGSERIAL PRIMARY KEY,
                                     order_id BIGINT NOT NULL,
                                     order_number VARCHAR(50) NOT NULL,
                                     payment_status VARCHAR(50) NOT NULL,
                                     amount NUMERIC(10, 2) NOT NULL,
                                     payment_method VARCHAR(50),
                                     created_at TIMESTAMP NOT NULL DEFAULT NOW());
