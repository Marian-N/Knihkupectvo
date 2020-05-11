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
        int inserted = 0;
        int total = 0;
        int total_count = count;
        long startTime = System.currentTimeMillis();
        System.out.println("Starting orders seeder.");
        while(count-- > 0) {
            statement.setDate(1, RandomGenerator.getRandomDate(1989, 2020));
            statement.setDouble(2, RandomGenerator.getRandomPrice());
            statement.setString(3, RandomGenerator.getRandomStatus());
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, IDs.size() - 1);
            statement.setInt(4, IDs.get(randomIndex));
            statement.addBatch();
            inserted++;
            total++;
            if(inserted % 100 == 0) {
                System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
                statement.executeBatch();
                inserted = 0;
            }
        }
        long endTime = System.currentTimeMillis();
        float time = (endTime - startTime) / 1000F;
        if(inserted > 0) {
            System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
            statement.executeBatch();
        }
        System.out.println("Orders seeder finished successfully after " + time + "s.");
        statement.close();
    }
}
