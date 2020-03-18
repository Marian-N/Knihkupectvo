package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;
import utils.RandomGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderBookSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        PreparedStatement statement = null;
        DatabaseUtils databaseUtils = DatabaseUtils.getInstance();
        RandomGenerator generator = RandomGenerator.getInstance();
        databaseUtils.emptyTable(connection, "order_book");
        List<Integer> orderIDs = databaseUtils.getTableIDs(connection, "orders");
        List<Integer> bookIDs = databaseUtils.getTableIDs(connection, "books");
        for(int i = 0; i < count; i++) {
            statement = connection.prepareStatement("INSERT INTO order_book" +
                    "(quantity, book_id, order_id) VALUES (?, ?, ?)");
            statement.setInt(1, generator.getRandomIntFromInterval(1, 10));
            statement.setInt(2, bookIDs.get(generator.getRandomIntFromInterval(0, bookIDs.size())));
            statement.setInt(3, orderIDs.get(generator.getRandomIntFromInterval(0, orderIDs.size())));
            statement.executeUpdate();
        }
        statement.close();
    }
}
