package Model;

public class BookGenre {
    private int genreID;
    private int bookID;

    public BookGenre(int genreID, int bookID) {
        this.genreID = genreID;
        this.bookID = bookID;
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
}
