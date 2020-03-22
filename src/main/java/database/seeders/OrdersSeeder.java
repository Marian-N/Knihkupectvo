package database.seeders;

import database.Database;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrdersSeeder {
    public static void run(Connection connection, Integer count) throws SQLException {
        String query = "INSERT INTO orders (date, price, status, customer_id) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("orders");
        List<Integer> IDs = Database.getTableIDs("customers");
        while(count-- > 0) {
            statement.setDate(1, RandomGenerator.getRandomDate(1989, 2020));
            statement.setDouble(2, RandomGenerator.getRandomPrice());
            statement.setString(3, RandomGenerator.getRandomStatus());
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, IDs.size() - 1);
            statement.setInt(4, IDs.get(randomIndex));
            statement.executeUpdate();
        }
        statement.close();
    }
}
