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
        int inserted = 0;
        int total = 0;
        int total_count = numberOfOrders;
        long startTime = System.currentTimeMillis();
        System.out.println("Starting OrderBook seeder.");
        for(int i = 1; i <= numberOfOrders; i++) {
            int numberOfBooksInOrder = RandomGenerator.getRandomIntFromInterval(1, 4);
            total_count += numberOfBooksInOrder - 1;
            while(numberOfBooksInOrder-- > 0){
                int bookQuantity = RandomGenerator.getRandomIntFromInterval(1, 5);
                statement.setInt(1, bookQuantity);
                int randomIndex = RandomGenerator.getRandomIntFromInterval(0, bookIDs.size() - 1);
                statement.setInt(2, bookIDs.get(randomIndex));
                statement.setInt(3, i);
                statement.addBatch();
                inserted++;
                total++;
                if(inserted % 100 == 0) {
                    System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
                    statement.executeBatch();
                    inserted = 0;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        float time = (endTime - startTime) / 1000F;
        if(inserted > 0) {
            System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
            statement.executeBatch();
        }
        System.out.println("OrderBook seeder finished successfully after " + time + "s.");
        statement.close();
    }
}
