package model;

import database.Database;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

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
    }

    public ObservableList<OrderContent> getOrderContents() {
        return orderContents;
    }

    public void setOrderContents(ObservableList<OrderContent> orderContent) {
        this.orderContents = orderContents;
    }
}
