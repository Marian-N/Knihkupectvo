package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;

public class OrdersController {
    private static OrdersController instance = null;
    private Connection connection;

    private OrdersController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static OrdersController getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null) instance = new OrdersController();
        return instance;
    }

    public void changeStatus(Order order, String status) throws SQLException, ClassNotFoundException {
        order.setStatus(status);
    }

    /**
     * Returns ObservableList od OrderContent from order with id order_id, with books and quantity
     * @param order_id is id of order
     * @return ObservableList of order contents(model Book and quantity)
     */
    private ObservableList<OrderContent> getOrderContents(int order_id) throws SQLException, ClassNotFoundException {
        ObservableList<OrderContent> orderContents = FXCollections.observableArrayList();
        String query = String.format("SELECT ob.book_id, ob.quantity, " +
                "b.title, b.price, b.stock_quantity, b.publication_date, b.description, " +
                "p.id publisher_id, p.name publisher_name " +
                "FROM order_book ob " +
                "JOIN books b ON b.id=ob.book_id " +
                "JOIN publishers p ON p.id=b.publisher_id " +
                "WHERE ob.order_id=%s;", order_id);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            int id = resultSet.getInt("book_id");
            int quantity = resultSet.getInt("quantity");
            String title = resultSet.getString("title");
            double price = resultSet.getDouble("price");
            int stockQuantity = resultSet.getInt("stock_quantity");
            Date publicationDate = resultSet.getDate("publication_date");
            String description = resultSet.getString("description");
            int publisherID = resultSet.getInt("publisher_id");
            String publisherName = resultSet.getString("publisher_name");
            Publisher publisher = new Publisher(publisherID, publisherName);
            Genres genres = GenresController.getInstance().getBookGenres(id);
            Book book = new Book(id, title, price, stockQuantity, publisher, publicationDate, description, genres);
            orderContents.add(new OrderContent(book, quantity));
        }
        resultSet.close();
        statement.close();

        return orderContents;
    }

    /**
     * Returns observable list with all orders of customer with id customer_id
     * @param customer_id
     * @return ObservableList of orders
     */
    public ObservableList<Order> getOrders(int customer_id) throws SQLException, ClassNotFoundException {
        CustomerController cc = CustomerController.getInstance();
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String query = String.format("SELECT o.id, o.date, o.price, o.status, o.customer_id, " +
                "c.first_name, c.last_name, c.mail, c.city, c.zip, c.address " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id=c.id " +
                "WHERE o.customer_id=%s;", customer_id);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            int id = resultSet.getInt("id");
            Date date = resultSet.getDate("date");
            double price = resultSet.getDouble("price");
            String status = resultSet.getString("status");
            Customer customer = cc.getCustomerFromRS(resultSet);
            ObservableList orderContents = getOrderContents(id);
            orders.add(new Order(id, date, customer, price, status, orderContents));
        }
        resultSet.close();
        statement.close();

        return orders;
    }

    public boolean hasOrders(int bookID) throws SQLException {
        String query = "SELECT COUNT(ob.book_id) FROM order_book ob " +
                "WHERE ob.book_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, bookID);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        statement.close();

        return count > 0;
    }
}
