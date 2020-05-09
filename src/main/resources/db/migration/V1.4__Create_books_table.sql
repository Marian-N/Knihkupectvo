CREATE TABLE books(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DECIMAL(6, 2) NOT NULL,
    stock_quantity INT,
    publication_date DATE NOT NULL,
    publisher_id INT NOT NULL,
    description TEXT,
    CONSTRAINT fk_publisher_id
        FOREIGN KEY(publisher_id)
        REFERENCES publishers(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

