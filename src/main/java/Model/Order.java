package Model;

import java.sql.Date;

public class Order {
    private int ID;
    private Date date;
    private int customerID;
    private double price;
    private String status;

    public Order(int ID, Date date, int customerID, double price, String status) {
        this.ID = ID;
        this.date = date;
        this.customerID = customerID;
        this.price = price;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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

    public void setStatus(String status) {
        this.status = status;
    }
}
