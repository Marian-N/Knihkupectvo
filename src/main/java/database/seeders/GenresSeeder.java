package database.seeders;

import com.github.javafaker.Faker;
import database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenresSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        String query = "INSERT INTO genres (name) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("genres");
        int inserted = 0;
        int total = 0;
        int total_count = count;
        long startTime = System.currentTimeMillis();
        System.out.println("Starting genres seeder.");
        while(count-- > 0) {
            statement.setString(1, faker.book().genre());
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
        System.out.println("Genres seeder finished successfully after " + time + "s.");
        statement.close();
    }
}
