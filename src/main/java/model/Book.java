package model;

import java.sql.Date;

public class Book {
    private int ID;
    private String title;
    private double price;
    private int stockQuantity;
    private Date publicationDate;
    private int publisherID;
    private String description;

    public Book(int ID, String title, double price, int stockQuantity, Date publicationDate, int publisherID, String description) {
        this.ID = ID;
        this.title = title;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.publicationDate = publicationDate;
        this.publisherID = publisherID;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
