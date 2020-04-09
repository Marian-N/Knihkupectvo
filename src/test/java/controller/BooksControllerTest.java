package controller;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class BooksControllerTest {

    @Test
    public void testGetBooks() throws SQLException, ClassNotFoundException {
        BooksController bk = BooksController.getInstance();
        assertNotNull("There are no data in table books", bk.getBooks(0, "id"));
        assertNull("Function BooksController.getBooks returned value when it should't",
                bk.getBooks(-1, "id"));
    }
}