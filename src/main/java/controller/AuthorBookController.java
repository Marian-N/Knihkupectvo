package controller;

import database.Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuthorBookController {
    private static AuthorBookController _instance = null;
    private static Connection connection;

    private AuthorBookController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    /**
     * Returns IDs of all authors with bookID
     * @param bookID
     * @return List of author IDs
     * @throws SQLException
     */
    public static List<Integer> getAuthors(int bookID) throws SQLException {
        List<Integer> IDs = new ArrayList<>();
        String query = String.format("SELECT author_id FROM author_book WHERE book_id = %d", bookID);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("author_id"));
        }
        statement.close();
        resultSet.close();
        return IDs;
    }

    public static AuthorBookController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new AuthorBookController();
        return _instance;
    }
}
