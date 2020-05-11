package database.seeders;

import database.Database;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuthorBookSeeder {
    public static void run(Connection connection) throws SQLException {
        String query = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("author_book");
        List<Integer> authorIDs = Database.getTableIDs("authors");
        List<Integer> bookIDs = Database.getTableIDs("books");
        int inserted = 0;
        int numberOfBooks = Database.getRowsCount("books");
        int numberOfAuthors = Database.getRowsCount("authors");
        int total = 0;
        int total_count = numberOfAuthors + numberOfBooks;
        long startTime = System.currentTimeMillis();
        System.out.println("Starting AuthorBook seeder.");
        for(int i = 1; i <= numberOfBooks; i++) {
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, authorIDs.size() - 1);
            statement.setInt(1, authorIDs.get(randomIndex));
            statement.setInt(2, i);
            statement.addBatch();
            inserted++;
            total++;
            if(inserted % 100 == 0) {
                System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
                statement.executeBatch();
                inserted = 0;
            }
        }
        if(inserted > 0) {
            System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
            statement.executeBatch();
        }
        inserted = 0;
        for(int i = 1; i <= numberOfAuthors; i++){
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, bookIDs.size() - 1);
            statement.setInt(1, i);
            statement.setInt(2, bookIDs.get(randomIndex));
            statement.addBatch();
            inserted++;
            total++;
            if(inserted % 100 == 0) {
                System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
                statement.executeBatch();
                inserted = 0;
            }
        }
        long endTime = System.currentTimeMillis();
        float time = (endTime - startTime) / 1000F;
        if(inserted > 0) {
            System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
            statement.executeBatch();
        }
        System.out.println("AuthorBook seeder finished successfully after " + time + "s.");
        statement.close();
    }
}
