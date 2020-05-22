package model;

import database.Database;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Genres {
    private List<Genre> genres;
    private SimpleObjectProperty<String> stringGenres;

    /**
     * Create genres for book with given ID
     * @param bookID
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Genres(int bookID) throws SQLException, ClassNotFoundException {
        genres = new ArrayList<>();
        String query = "SELECT g.id genre_id, g.name genre_name " +
                "FROM book_genre bg " +
                "JOIN genres g ON g.id=bg.genre_id " +
                "WHERE bg.book_id = ?";
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(query);
        preparedStatement.setInt(1, bookID);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> genreNames = new ArrayList<>();
        while(resultSet.next()) {
            genres.add(new Genre(resultSet));
            genreNames.add(resultSet.getString(2));
        }
        resultSet.close();
        preparedStatement.close();
        stringGenres = new SimpleObjectProperty(String.join(", ", genreNames));
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getStringGenres() {
        return stringGenres.get();
    }

    public SimpleObjectProperty<String> stringGenresProperty() {
        return stringGenres;
    }
}
