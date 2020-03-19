package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorsSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        String query = "INSERT INTO authors (name) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(query);
        DatabaseUtils.emptyTable(connection, "authors");
        while(count-- > 0) {
            statement.setString(1, faker.book().author());
            statement.executeUpdate();
        }
        statement.close();
    }
}
