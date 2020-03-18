package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrdersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        PreparedStatement statement = null;
        DatabaseUtils databaseUtils = DatabaseUtils.getInstance();
        RandomGenerator generator = RandomGenerator.getInstance();
        databaseUtils.emptyTable(connection, "orders");

        for(int i = 0; i < count; i++) {
            statement = connection.prepareStatement("INSERT INTO orders " +
                    "(date, price, status, customer_id) VALUES (?, ?, ?, ?)");
            statement.setDate(1, generator.getRandomDate(1989, 2020));
            statement.setDouble(2, generator.getRandomPrice());
            statement.setString(3, generator.getRandomStatus());
            statement.setInt(4, 1);
            statement.executeUpdate();
        }
        statement.close();
    }
}
