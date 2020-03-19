package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        PreparedStatement statement = null;
        DatabaseUtils databaseUtils = DatabaseUtils.getInstance();
        databaseUtils.emptyTable(connection, "customers");
        statement = connection.prepareStatement("INSERT INTO customers " +
                "(first_name, last_name, mail, city, zip, address) VALUES (?, ?, ?, ?, ?, ?)");
        for(int i = 0; i < count; i++) {
            statement.setString(1, faker.name().firstName());
            statement.setString(2, faker.name().lastName());
            statement.setString(3, faker.internet().emailAddress());
            statement.setString(4, faker.address().city());
            statement.setString(5, faker.address().zipCode());
            statement.setString(6, faker.address().streetAddress());
            statement.executeUpdate();
        }
        statement.close();
    }
}
