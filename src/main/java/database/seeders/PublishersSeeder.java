package database.seeders;

import com.github.javafaker.Faker;
import database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PublishersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        String query = "INSERT INTO publishers (name) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("publishers");
        int inserted = 0;
        while(count-- > 0) {
            statement.setString(1, faker.book().publisher());
            statement.addBatch();
            inserted++;
            if(inserted % 100 == 0) {
                statement.executeBatch();
                inserted = 0;
            }
        }
        if(inserted > 0) statement.executeBatch();
        statement.close();
    }
}