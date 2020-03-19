package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PublishersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        String query = "INSERT INTO publishers (name) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(query);
        DatabaseUtils.emptyTable(connection, "publishers");
        while(count-- > 0) {
            statement.setString(1, faker.book().publisher());
            statement.executeUpdate();
        }
        statement.close();
    }
}