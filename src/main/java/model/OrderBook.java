package model;

public class OrderBook {
    private int orderID;
    private int bookID;
    private int quantity;

    public OrderBook(int orderID, int bookID, int quantity) {
        this.orderID = orderID;
        this.bookID = bookID;
        this.quantity = quantity;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
