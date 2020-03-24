package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Genre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenresController {
    private static GenresController _instance = null;
    private Database database;
    private Connection connection;
    private ObservableMap<Integer, Genre> genres = FXCollections.observableHashMap();

    public GenresController() throws SQLException, ClassNotFoundException {
        database = Database.get_instance();
        connection = database.getConnection();
        String query = "SELECT * from genres";
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Genre genre = new Genre(id, name);
            genres.put(id, genre);
        }
        resultSet.close();
    }

    public static GenresController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new GenresController();
        return _instance;
    }

    public ObservableMap<Integer, Genre> getGenres() {
        return genres;
    }
}
