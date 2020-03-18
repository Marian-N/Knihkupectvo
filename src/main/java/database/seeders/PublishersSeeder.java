package database.seeders;

import com.github.javafaker.Faker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PublishersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("truncate publishers restart identity cascade;");
        statement.executeUpdate();
        for(int i = 0; i < count; i++) {
            statement = connection.prepareStatement("INSERT INTO publishers (name) VALUES (?)");
            statement.setString(1, faker.book().publisher());
            statement.executeUpdate();
        }
        statement.close();
    }
}
