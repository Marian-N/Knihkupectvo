CREATE TABLE book_genre(
    genre_id INT NOT NULL,
    book_id INT NOT NULL,
    CONSTRAINT fk_book_id
        FOREIGN KEY(book_id)
        REFERENCES books(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_genre_id
        FOREIGN KEY(genre_id)
        REFERENCES genres(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);