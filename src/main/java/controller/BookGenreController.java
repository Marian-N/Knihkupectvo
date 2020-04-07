package controller;

import database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookGenreController {
    private static BookGenreController _instance = null;
    private static Connection connection;

    private BookGenreController() throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        connection = database.getConnection();
    }

    public static List<Integer> getGenres(int bookID) throws SQLException {
        List<Integer> IDs = new ArrayList<>();
        String query = String.format("SELECT genre_id FROM book_genre WHERE book_id = %d", bookID);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("genre_id"));
        }
        statement.close();
        resultSet.close();
        return IDs;
    }

    public static List<Integer> getBooks(int authorID) throws SQLException {
        List<Integer> IDs = new ArrayList<>();
        String query = String.format("SELECT book_id FROM author_book WHERE author_id = %s", authorID);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("book_id"));
        }
        statement.close();
        resultSet.close();
        return IDs;
    }

    public static BookGenreController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new BookGenreController();
        return _instance;
    }
}
