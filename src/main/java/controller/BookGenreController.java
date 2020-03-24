package controller;

import database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookGenreController {
    private static BookGenreController _instance = null;
    private Database database;
    private static Connection connection;

    public BookGenreController() throws SQLException, ClassNotFoundException {
        database = Database.getInstance();
        connection = database.getConnection();
    }

    public static List<Integer> getGenres(int bookID) throws SQLException {
        List<Integer> IDs = new ArrayList<>();
        String query = String.format("SELECT genre_id FROM book_genre WHERE book_id = %d", bookID);
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("genre_id"));
        }
        resultSet.close();
        return IDs;
    }

    public static List<Integer> getBooks(int authorID) throws SQLException {
        List<Integer> IDs = new ArrayList<>();
        String query = String.format("SELECT book_id FROM author_book WHERE author_id = %s", authorID);
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("book_id"));
        }
        resultSet.close();
        return IDs;
    }

    public static BookGenreController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new BookGenreController();
        return _instance;
    }
}
