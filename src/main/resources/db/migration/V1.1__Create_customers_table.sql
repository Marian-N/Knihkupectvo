CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL,
    city VARCHAR(50),
    zip VARCHAR(15),
    address VARCHAR(255),
    role smallint,
    password VARCHAR(60)
);