package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Publisher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PublishersController {
    private static PublishersController _instance = null;
    private Connection connection;
    private ObservableMap<Integer, Publisher> publishers = FXCollections.observableHashMap();

    private PublishersController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
        String query = "SELECT * from publishers";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Publisher publisher = new Publisher(id, name);
            publishers.put(id, publisher);
        }
        statement.close();
        resultSet.close();
    }

    public static PublishersController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new PublishersController();
        return _instance;
    }

    public ObservableMap<Integer, Publisher> getPublishers() {
        return publishers;
    }

}
