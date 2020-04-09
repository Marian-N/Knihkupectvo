package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Book;
import model.Publisher;

import java.sql.*;

public class BooksController {
    private static BooksController _instance = null;
    private Connection connection;
    private ObservableMap<Integer, Book> books = FXCollections.observableHashMap();

    private BooksController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static BooksController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new BooksController();
        return _instance;
    }

    /**
     * Returns an ObservableMap which can be used in javaFX (eg. displaying books to table).
     * If page with given number does not exist return null
     * @param page the page for which the data should be returned
     * @param order the column name by which query should be ordered
     * @return observableMap with classes Book with their id as key
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableMap<Integer, Book> getBooks(int page, String order) throws SQLException, ClassNotFoundException {
        if(page < 0) return null;
        int booksPerPage = 100;
        int offset = booksPerPage * page;
        String query = String.format("SELECT * FROM books ORDER BY %s " +
                "OFFSET %s ROWS FETCH FIRST %s ROW ONLY;", order, offset, booksPerPage);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        PublishersController publishersController = PublishersController.getInstance();
        ObservableMap<Integer, Publisher> publishers = publishersController.getPublishers();
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            System.out.println(id);
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
        statement.close();
        resultSet.close();

        return books;
    }
    public ObservableMap<Integer, Book> getBooks() { return null;};
}


