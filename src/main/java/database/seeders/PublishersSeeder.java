package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PublishersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        PreparedStatement statement = null;
        DatabaseUtils databaseUtils = DatabaseUtils.getInstance();
        databaseUtils.emptyTable(connection, "publishers");
        for(int i = 0; i < count; i++) {
            statement = connection.prepareStatement("INSERT INTO publishers (name) VALUES (?)");
            statement.setString(1, faker.book().publisher());
            statement.executeUpdate();
        }
        statement.close();
    }
}
