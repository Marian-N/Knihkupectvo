package database.seeders;

import database.Database;
import utils.RandomGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookGenreSeeder {
    public static void run(Connection connection) throws SQLException {
        String query = "INSERT INTO book_genre (genre_id, book_id) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("book_genre");
        List<Integer> genreIDs = Database.getTableIDs("genres");
        List<Integer> bookIDs = Database.getTableIDs("books");
        int numberOfBooks = Database.getRowsCount("books");
        for(int i = 1; i <= numberOfBooks; i++) {
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, genreIDs.size() - 1);
            statement.setInt(1, genreIDs.get(randomIndex));
            statement.setInt(2, i);
            statement.executeUpdate();
        }
        int numberOfGenres = Database.getRowsCount("genres");
        for(int i = 1; i <= numberOfGenres; i++){
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, bookIDs.size() - 1);
            statement.setInt(1, i);
            statement.setInt(2, bookIDs.get(randomIndex));
            statement.executeUpdate();
        }
        statement.close();
    }
}
