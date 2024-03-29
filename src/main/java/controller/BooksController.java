package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Book;
import model.Genres;
import model.Publisher;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class BooksController {
    private Logger logger = LoggerFactory.getLogger(BooksController.class);
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

    /**
     * Return books with pagination and count of all books that matches name pattern for displaying number of pages
     * @param resultSet
     * @return [int count, ObservableList<Book> books]
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private Object[] getListWithCount(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        int count = 0;

        while(resultSet.next()){
            count = resultSet.getInt(1);
            int id = resultSet.getInt(2);
            String title = resultSet.getString(3);
            double price = resultSet.getDouble(4);
            int stockQuantity = resultSet.getInt(5);
            Date publicationDate = resultSet.getDate(6);
            String description = resultSet.getString(8);
            Publisher publisher = new Publisher(resultSet);
            Genres genres = GenresController.getInstance().getBookGenres(id);
            Book book = new Book(id, title, price, stockQuantity, publisher, publicationDate, description, genres);
            books.add(book);
        }
        resultSet.close();

        Object[] result = new Object[2];
        result[0] = count;
        result[1] = books;

        return result;
    }

    /**
     * Return observable list of books from result set
     * @param resultSet
     * @return ObservableList<Book> books
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private ObservableList<Book> getList(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        ObservableList<Book> books = FXCollections.observableArrayList();

        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String title = resultSet.getString(2);
            double price = resultSet.getDouble(3);
            int stockQuantity = resultSet.getInt(4);
            Date publicationDate = resultSet.getDate(5);
            String description = resultSet.getString(7);
            Publisher publisher = new Publisher(resultSet);
            Genres genres = GenresController.getInstance().getBookGenres(id);
            Book book = new Book(id, title, price, stockQuantity, publisher, publicationDate, description, genres);
            books.add(book);
        }
        resultSet.close();

        return books;
    }

    /**
     * @param resultSet from which data will be added to books map
     * @return ObservableMap with all books from result set
     */
    private ObservableMap<Integer, Book> getObservableMap(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        ObservableMap<Integer, Book> books = FXCollections.observableHashMap();

        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            double price = resultSet.getDouble("price");
            int stockQuantity = resultSet.getInt("stock_quantity");
            Date publicationDate = resultSet.getDate("publication_date");
            String description = resultSet.getString("description");
            Publisher publisher = new Publisher(resultSet);
            Genres genres = GenresController.getInstance().getBookGenres(id);
            Book book = new Book(id, title, price, stockQuantity, publisher, publicationDate, description, genres);
            books.put(id, book);
        }
        resultSet.close();

        return books;
    }

    /**
     * Return observable list of book ordered by author, popularity or default(id) on given page
     * @param page
     * @param orderBy
     * @param desc
     * @return ObservableList of books
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private ObservableList<Book> executeQuery(int page, String orderBy, Boolean desc) throws SQLException, ClassNotFoundException {
        if(page < 0) return null;
        int booksPerPage = 100;
        int offset = booksPerPage * page;
        String query;
        String order = "ASC";
        if(desc) order = "DESC";
        if(orderBy.equals("author")){
            query = String.format("SELECT b.*, p.name publisher_name FROM books b " +
                    "JOIN author_book ab ON b.id=ab.book_id " +
                    "JOIN authors a ON a.id=ab.author_id " +
                    "JOIN publishers p ON p.id=b.publisher_id " +
                    "ORDER BY a.name %s " +
                    "OFFSET %s ROWS " +
                    "FETCH FIRST %s ROWS ONLY;", order, offset, booksPerPage);
        }
        else if(orderBy.equals("popularity")){
            query = String.format("SELECT b.*, p.name publisher_name " +
                    "FROM order_book ob " +
                    "JOIN books b ON ob.book_id = b.id " +
                    "JOIN publishers p ON p.id=b.publisher_id " +
                    "WHERE b.stock_quantity>0 " +
                    "GROUP BY b.id, p.name " +
                    "HAVING COUNT(ob.book_id)>=1 " +
                    "ORDER BY COUNT(ob.book_id) %s " +
                    "OFFSET %s ROWS " +
                    "FETCH FIRST %s ROWS ONLY;", order, offset, booksPerPage);
        }
        else {
            query = String.format("SELECT b.*, p.name publisher_name FROM books b " +
                    "JOIN publishers p ON p.id=b.publisher_id " +
                    "ORDER BY %s %s " +
                    "OFFSET %s ROWS FETCH FIRST %s ROW ONLY;", orderBy, order, offset, booksPerPage);
        }
        Statement statement = connection.createStatement();
        statement.setFetchSize(100);
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
     * Return all books that matches patter %title% on given page and count of all books that match pattern
     * @param title
     * @param page
     * @return [int count, ObservableList<Book>]
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Object[] findBook(String title, int page) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) OVER() as count," +
                "b.*, p.name publisher_name FROM books b " +
                "JOIN publishers p ON p.id=b.publisher_id " +
                "WHERE LOWER(title) LIKE ?" +
                "ORDER BY b.id " +
                "OFFSET ? ROWS " +
                "FETCH FIRST ? ROWS ONLY";
        int booksPerPage = 100;
        int offset = booksPerPage * page;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, String.format("%c%s%c", '%', title.toLowerCase(), '%'));
        statement.setInt(2, offset);
        statement.setInt(3, booksPerPage);
        ResultSet resultSet = statement.executeQuery();
        Object[] result = getListWithCount(resultSet);
        statement.close();

        return result;
    }

    /**
     * Change price of book in database
     * @param bookID
     * @param price
     */
    public void changeBook(int bookID, double price) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Book book = session.get(Book.class, bookID);
        logger.info(String.format("Changed book(ID = %d) price from %.2f to %.2f.", bookID, book.getPrice(), price));
        book.setPrice(price);
        session.saveOrUpdate(book);
        transaction.commit();
        session.close();
    }

    /**
     * Change stock quantity of book in database
     * @param bookID
     * @param quantity
     */
    public void changeBook(int bookID, int quantity) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Book book = session.get(Book.class, bookID);
        logger.info(String.format("Changed book(ID = %d) quantity from %d to %d.", bookID, book.getStockQuantity(), quantity));
        book.setStockQuantity(quantity);
        session.saveOrUpdate(book);
        transaction.commit();
        session.close();
    }

    /**
     * Remove book from database if it does not have any orders
     * @param bookID
     * @return true if successful
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean removeBook(int bookID) throws SQLException, ClassNotFoundException {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Book book = session.get(Book.class, bookID);
        if(book != null && !OrdersController.getInstance().hasOrders(bookID)) {
            session.delete(book);
            transaction.commit();
            session.close();
            logger.info(String.format("Removed book(ID = %d).", bookID));
            return true;
        }
        transaction.commit();
        session.close();
        return false;
    }

    /**
     * Add book to database
     * @param book
     */
    public void addBook(Book book) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        if(book != null) {
            String message = String.format("Add new book, title = %s, price = %.2f, quantity = %d.",
                    book.getTitle(), book.getPrice(), book.getStockQuantity());
            logger.info(message);
            session.saveOrUpdate(book);
            transaction.commit();
        }
        session.close();
    }
}


