CREATE TABLE author_book(
    book_id INT NOT NULL,
    author_id INT NOT NULL,
    CONSTRAINT fk_book_id
        FOREIGN KEY(book_id)
        REFERENCES books(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_author_id
        FOREIGN KEY(author_id)
        REFERENCES authors(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);