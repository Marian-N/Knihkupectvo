package database.seeders;

import database.Database;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderBookSeeder {
    public static void run(Connection connection) throws SQLException {
        String query = "INSERT INTO order_book (quantity, book_id, order_id) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("order_book");
        List<Integer> bookIDs = Database.getTableIDs("books");
        int numberOfOrders = Database.getRowsCount("orders");
        for(int i = 1; i <= numberOfOrders; i++) {
            int numberOfBooksInOrder = RandomGenerator.getRandomIntFromInterval(1, 4);
            while(numberOfBooksInOrder-- > 0){
                int bookQuantity = RandomGenerator.getRandomIntFromInterval(1, 5);
                statement.setInt(1, bookQuantity);
                int randomIndex = RandomGenerator.getRandomIntFromInterval(0, bookIDs.size() - 1);
                statement.setInt(2, bookIDs.get(randomIndex));
                statement.setInt(3, i);
                statement.executeUpdate();
            }
        }
        statement.close();
    }
}
