package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Genre;
import model.Genres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GenresController {
    private static GenresController _instance = null;
    private Connection connection;
    private ObservableMap<Integer, Genre> genres = FXCollections.observableHashMap();

    private GenresController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static GenresController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new GenresController();
        return _instance;
    }

    public Genres getBookGenres(int bookID) throws SQLException, ClassNotFoundException {
        return new Genres(bookID);
    }

    public ObservableMap<Integer, Genre> getGenres() throws SQLException {
        String query = "SELECT * from genres";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Genre genre = new Genre(id, name);
            genres.put(id, genre);
        }
        statement.close();
        resultSet.close();
        return genres;
    }
}
