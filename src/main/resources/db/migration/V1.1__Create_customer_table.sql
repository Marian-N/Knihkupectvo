CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    mail VARCHAR(255) NOT NULL,
    state VARCHAR(50),
    city VARCHAR(50),
    zip VARCHAR(15),
    address VARCHAR(255)
);