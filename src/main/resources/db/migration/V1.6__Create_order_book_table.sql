CREATE TABLE order_book(
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_order_id
        FOREIGN KEY(order_id)
        REFERENCES orders(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_book_id
        FOREIGN KEY(book_id)
        REFERENCES books(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);