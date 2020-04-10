package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Book;
import model.Publisher;

import java.sql.*;

public class BooksController {
    private static BooksController _instance = null;
    private Connection connection;

    private BooksController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static BooksController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new BooksController();
        return _instance;
    }

    private ObservableList<Book> getList(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        ObservableList<Book> books = FXCollections.observableArrayList();

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
            books.add(book);
        }
        resultSet.close();

        return books;
    }

    /**
     * @param resultSet from which data will be added to books map
     * @return ObservableMap with all books from result set
     */
    private ObservableMap<Integer, Book> handleResultSet(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        ObservableMap<Integer, Book> books = FXCollections.observableHashMap();

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

        return books;
    }

    private ObservableList<Book> executeQuery(int page, String orderBy, Boolean desc) throws SQLException, ClassNotFoundException {
        if(page < 0) return null;
        int booksPerPage = 100;
        int offset = booksPerPage * page;
        String query;
        String order = "ASC";
        if(desc) order = "DESC";
        if(orderBy.equals("author")){
            query = String.format("SELECT b.* FROM books b " +
                    "JOIN author_book ab ON b.id=ab.book_id " +
                    "JOIN authors a ON a.id=ab.author_id " +
                    "ORDER BY a.name %s " +
                    "OFFSET %s ROWS " +
                    "FETCH FIRST %s ROWS ONLY;", order, offset, booksPerPage);
        }
        else {
            query = String.format("SELECT * FROM books ORDER BY %s %s " +
                    "OFFSET %s ROWS FETCH FIRST %s ROW ONLY;", orderBy, order, offset, booksPerPage);
        }
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ObservableList<Book> books = getList(resultSet);
        statement.close();

        return books;
    }

    /**
     * Returns an ObservableList which can be used in javaFX (eg. displaying books to table).
     * If page with given number does not exist return null
     * @param page the page for which the data should be returned
     * @param order the column name by which query should be ordered
     * @return observableList with classes Book ordered by parameter order
     */
    public ObservableList<Book> getBooks(int page, String order) throws SQLException, ClassNotFoundException {
        return executeQuery(page, order, false);
    }

    /**
     * Returns an ObservableList which can be used in javaFX (eg. displaying books to table).
     * If page with given number does not exist return null
     * @param page the page for which the data should be returned
     * @param order the column name by which query should be ordered
     * @param desc true for descending order, false for ascending
     * @return observableList with classes Book ordered by parameter order
     */
    public ObservableList<Book> getBooks(int page, String order, Boolean desc) throws SQLException, ClassNotFoundException {
        return executeQuery(page, order, desc);
    }

    /**
     * @return all ObservableMap of all book from database
     */
    public ObservableMap<Integer, Book> getAllBooks() throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM books";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ObservableMap<Integer, Book> books = handleResultSet(resultSet);
        statement.close();

        return books;
    }
}


