package model;

public class OrderContent {
    private Book book;
    private int quantity;

    public OrderContent(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }
}
