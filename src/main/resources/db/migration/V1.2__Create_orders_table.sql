CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    customer_id INT NOT NULL,
    price DECIMAL(6, 2),
    status VARCHAR(15) NOT NULL,
    CONSTRAINT fk_customer_order
        FOREIGN KEY(customer_id)
        REFERENCES customers(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);