package database.seeders;

import com.github.javafaker.Faker;
import database.Database;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BooksSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        String query = "INSERT INTO books " +
                "(title, price, stock_quantity, publication_date, description, publisher_id)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("books");
        List<Integer> IDs = Database.getTableIDs("publishers");
        int inserted = 0;
        int total = 0;
        int total_count = count;
        long startTime = System.currentTimeMillis();
        System.out.println("Starting books seeder.");
        while(count-- > 0) {
            statement.setString(1, faker.book().title());
            statement.setDouble(2, RandomGenerator.getRandomPrice());
            statement.setInt(3, RandomGenerator.getRandomIntFromInterval(0, 100));
            statement.setDate(4, RandomGenerator.getRandomDate(1920, 2019));
            statement.setString(5, faker.backToTheFuture().quote());
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, IDs.size() - 1);
            statement.setInt(6, IDs.get(randomIndex));
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
        System.out.println("Books seeder finished successfully after " + time + "s.");
        statement.close();
    }
}
