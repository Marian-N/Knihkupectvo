package model;

public class OrderContent {
    public Book book;
    public int quantity;

    public OrderContent(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }
}
