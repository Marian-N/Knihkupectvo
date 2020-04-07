package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Author;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthorsController {
    private static AuthorsController _instance = null;
    private Connection connection;
    private ObservableMap<Integer, Author> authors = FXCollections.observableHashMap();

    private AuthorsController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
        String query = "SELECT * from authors";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Author author = new Author(id, name);
            authors.put(id, author);
        }
        statement.close();
        resultSet.close();
    }

    public static AuthorsController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new AuthorsController();
        return _instance;
    }

    public ObservableMap<Integer, Author> getAuthors() {
        return authors;
    }
}
