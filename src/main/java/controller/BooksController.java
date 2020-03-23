package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Book;
import model.Publisher;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooksController {
    private static BooksController _instance = null;
    private Database database;
    private Connection connection;
    private ObservableMap<Integer, Book> books = FXCollections.observableHashMap();

    public BooksController() throws SQLException, ClassNotFoundException {
        database = Database.get_instance();
        connection = Database.getConnection();
        String query = "SELECT * from books";
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        PublishersController publishersController = PublishersController.getInstance();
        ObservableMap<Integer, Publisher> publishers = publishersController.getPublishers();
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            double price = resultSet.getDouble("price");
            int stockQuantity = resultSet.getInt("stock_quantity");
            Date publicationDate = resultSet.getDate("publication_date");
            String description = resultSet.getString("description");
            int publisherID = resultSet.getInt("publisher_id");
            Publisher publisher = publishers.get(publisherID);
            Book book = new Book(id, title, price, stockQuantity, publisher, publicationDate, description);
            books.put(id, book);
        }
        resultSet.close();
    }

    public static BooksController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new BooksController();
        return _instance;
    }

    public ObservableMap<Integer, Book> getBooks() {
        return books;
    }
}
