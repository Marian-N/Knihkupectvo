package model;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Order {
    private Logger logger = LoggerFactory.getLogger(Order.class);
    private int ID;
    private Date date;
    private Customer customer;
    private double price;
    private String status;
    private ObservableList<OrderContent> orderContents;

    public Order(int ID, Date date, Customer customer, double price, String status, ObservableList orderContents) {
        this.ID = ID;
        this.date = new Date(date.getTime());
        this.customer = customer;
        this.price = price;
        this.status = status;
        this.orderContents = orderContents;
    }

    /**
     * Create new order in database
     * @param customer
     * @param orderContents
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Order(Customer customer, ObservableList<OrderContent> orderContents) throws SQLException, ClassNotFoundException {
        this.customer = customer;
        this.orderContents = FXCollections.observableArrayList(orderContents);
        this.date = new Date(System.currentTimeMillis());
        this.price = getTotalPrice();
        this.status = "nevybavená";
        String query = "INSERT INTO orders " +
                "(date, customer_id, price, status) " +
                " VALUES (?, ?, ?, ?)";
        Connection connection = Database.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setDate(1, date);
        statement.setInt(2, customer.getID());
        statement.setDouble(3, price);
        statement.setString(4, status);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        this.ID = rs.getInt(1);
        rs.close();
        statement.close();
        addToOrderBook();
        String message = String.format("User %s(ID = %d) created order(ID = %d), price = %.2f.",
                customer.getMail(), customer.getID(), this.ID, this.price);
        logger.info(message);
    }

    /**
     * Add order contents to many-to-many table order_book
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void addToOrderBook() throws SQLException, ClassNotFoundException {
        String insertQuery = "INSERT INTO order_book (order_id, book_id, quantity) VALUES (?, ?, ?)";
        String quantityQuery = "UPDATE books SET stock_quantity = stock_quantity - ? WHERE id=?";
        Connection connection = Database.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        PreparedStatement changeQuantity = connection.prepareStatement(quantityQuery);
        for(OrderContent orderContent:orderContents) {
            int quantity = orderContent.getQuantity();
            Book book = orderContent.getBook();
            book.setStockQuantity(book.getStockQuantity() - quantity);
            statement.setInt(1, this.ID);
            statement.setInt(2, orderContent.getBook().getID());
            statement.setInt(3, orderContent.getQuantity());
            statement.addBatch();
            changeQuantity.setInt(1, orderContent.getQuantity());
            changeQuantity.setInt(2, orderContent.getBook().getID());
            changeQuantity.addBatch();
        }
        statement.executeBatch();
        statement.close();
        changeQuantity.executeBatch();
        changeQuantity.close();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private double getTotalPrice() {
        double total = 0;
        for(OrderContent orderContent:orderContents)
            total += orderContent.getBook().getPrice() * orderContent.getQuantity();
        return total;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Set status of order
     * @param status
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void setStatus(String status) throws SQLException, ClassNotFoundException {
        String query = String.format("UPDATE orders " +
                "SET status = \'%s\' " +
                "WHERE id = %s;", status, this.ID);
        String message = String.format("Order(ID = %d) status changed from %s to %s",
                this.ID, this.status, status);
        logger.info(message);
        this.status = status;
        Database.getInstance().executeQuery(query);

        if(status.equals("zamietnutá") || status.equals("zrušená")){
            String quantityQuery = "UPDATE books SET stock_quantity = stock_quantity + ? WHERE id=?";
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement changeQuantity = connection.prepareStatement(quantityQuery);
            for(OrderContent orderContent:orderContents) {
                int quantity = orderContent.getQuantity();
                Book book = orderContent.getBook();
                book.setStockQuantity(book.getStockQuantity() + quantity);
                changeQuantity.setInt(1, quantity);
                changeQuantity.setInt(2, book.getID());
                changeQuantity.addBatch();
            }
            changeQuantity.executeBatch();
            changeQuantity.close();
        }
    }

    public ObservableList<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(ObservableList<OrderContent> orderContent) {
        this.orderContents = orderContents;
    }
}
