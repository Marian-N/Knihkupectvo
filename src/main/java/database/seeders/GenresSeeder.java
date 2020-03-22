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
        while(count-- > 0) {
            statement.setString(1, faker.book().genre());
            statement.executeUpdate();
        }
        statement.close();
    }
}
