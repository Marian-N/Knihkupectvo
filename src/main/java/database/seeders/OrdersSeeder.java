package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrdersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        PreparedStatement statement = null;
        DatabaseUtils databaseUtils = DatabaseUtils.getInstance();
        RandomGenerator generator = RandomGenerator.getInstance();
        databaseUtils.emptyTable(connection, "orders");
        List<Integer> IDs = databaseUtils.getTableIDs(connection, "customers");

        for(int i = 0; i < count; i++) {
            statement = connection.prepareStatement("INSERT INTO orders " +
                    "(date, price, status, customer_id) VALUES (?, ?, ?, ?)");
            statement.setDate(1, generator.getRandomDate(1989, 2020));
            statement.setDouble(2, generator.getRandomPrice());
            statement.setString(3, generator.getRandomStatus());
            statement.setInt(4, IDs.get(generator.getRandomIntFromInterval(0, IDs.size())));
            statement.executeUpdate();
        }
        statement.close();
    }
}
