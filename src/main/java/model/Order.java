package model;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Order {
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
    }

    private void addToOrderBook() throws SQLException, ClassNotFoundException {
        String insertQuery = "INSERT INTO order_book (order_id, book_id, quantity) VALUES (?, ?, ?)";
        String quantityQuery = "UPDATE books SET stock_quantity = stock_quantity - ? WHERE id=?";
        Connection connection = Database.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        PreparedStatement changeQuantity = connection.prepareStatement(quantityQuery);
        for(OrderContent orderContent:orderContents) {
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

    public void setStatus(String status) throws SQLException, ClassNotFoundException {
        String query = String.format("UPDATE orders " +
                "SET status = \'%s\' " +
                "WHERE id = %s;", status, this.ID);
        Database.getInstance().executeQuery(query);
        this.status = status;

        if(status.equals("zamietnutá") || status.equals("zrušená")){
            String quantityQuery = "UPDATE books SET stock_quantity = stock_quantity + ? WHERE id=?";
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement changeQuantity = connection.prepareStatement(quantityQuery);
            for(OrderContent orderContent:orderContents) {
                changeQuantity.setInt(1, orderContent.getQuantity());
                changeQuantity.setInt(2, orderContent.getBook().getID());
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
