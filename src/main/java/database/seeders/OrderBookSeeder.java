package database.seeders;

import utils.DatabaseUtils;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderBookSeeder {
    public static void run(Connection connection) throws SQLException {
        PreparedStatement statement = null;
        DatabaseUtils databaseUtils = DatabaseUtils.getInstance();
        RandomGenerator generator = RandomGenerator.getInstance();
        databaseUtils.emptyTable(connection, "order_book");
        List<Integer> bookIDs = databaseUtils.getTableIDs(connection, "books");
        int numberOfOrders = databaseUtils.getRowsCount(connection, "orders");
        statement = connection.prepareStatement("INSERT INTO order_book (quantity, book_id, order_id) VALUES (?, ?, ?)");
        for(int i = 1; i <= numberOfOrders; i++) {
            int numberOfBooksInOrder = generator.getRandomIntFromInterval(1, 7);
            while(numberOfBooksInOrder-- > 0){
                statement.setInt(1, generator.getRandomIntFromInterval(1, 10));
                statement.setInt(2, bookIDs.get(generator.getRandomIntFromInterval(0, bookIDs.size() - 1)));
                statement.setInt(3, i);
                statement.executeUpdate();
            }
        }
        statement.close();
    }
}
