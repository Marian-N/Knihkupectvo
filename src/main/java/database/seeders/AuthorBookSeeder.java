package database.seeders;

import utils.DatabaseUtils;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuthorBookSeeder {
    public static void run(Connection connection) throws SQLException {
        String query = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        DatabaseUtils.emptyTable(connection, "author_book");
        List<Integer> authorIDs = DatabaseUtils.getTableIDs(connection, "authors");
        List<Integer> bookIDs = DatabaseUtils.getTableIDs(connection, "books");
        int numberOfBooks = DatabaseUtils.getRowsCount(connection, "books");
        for(int i = 1; i <= numberOfBooks; i++) {
            int numberOfAuthors = RandomGenerator.getRandomIntFromInterval(1, 2);
            while(numberOfAuthors-- > 0){
                int randomIndex = RandomGenerator.getRandomIntFromInterval(0, authorIDs.size() - 1);
                statement.setInt(1, authorIDs.get(randomIndex));
                statement.setInt(2, i);
                statement.executeUpdate();
            }
        }
        int numberOfAuthors = DatabaseUtils.getRowsCount(connection, "authors");
        for(int i = 1; i <= numberOfAuthors; i++){
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, authorIDs.size() - 1);
            statement.setInt(1, i);
            statement.setInt(2, bookIDs.get(randomIndex));
            statement.executeUpdate();
        }
        statement.close();
    }
}
