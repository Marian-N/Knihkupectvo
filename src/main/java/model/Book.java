package model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "id")
    private int ID;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private double price;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "publication_date")
    private Date publicationDate;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    @Transient
    private Genres genres;

    public Genres getGenres() {
        return genres;
    }

    public Book(int ID, String title, double price, int stockQuantity, Publisher publisher,
                Date publicationDate, String description, Genres genres) {
        this.ID = ID;
        this.title = title;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.publisher = publisher;
        this.publicationDate = new Date(publicationDate.getTime());
        this.description = description;
        this.genres = genres;
    }

    public Book() {}

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
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
        return new Date(publicationDate.getTime());
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = new Date(publicationDate.getTime());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
