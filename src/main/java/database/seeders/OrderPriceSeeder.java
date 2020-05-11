package database.seeders;

import controller.BooksController;
import controller.OrderBookController;
import database.Database;
import javafx.collections.ObservableMap;
import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderPriceSeeder {
    public static void run(Connection connection) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        ObservableMap<Integer, Book> books = BooksController.getInstance().getAllBooks();
        String query = "UPDATE orders SET price = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        List<Integer> orders = database.getTableIDs("orders");
        double orderPrice = 0;
        int inserted = 0;
        int total = 0;
        int total_count = orders.size();
        long startTime = System.currentTimeMillis();
        System.out.println("Starting OrderPrice seeder.");
        for(int order : orders){
            orderPrice = 0;
            List<int[]> orderContents = OrderBookController.getInstance().getOrderContents(order);
            for(int[] orderContent : orderContents){
                double bookPrice = books.get(orderContent[0]).getPrice();
                int bookQuantity = orderContent[1];
                orderPrice += bookPrice * bookQuantity;
            }
            orderPrice = (double) Math.round(orderPrice * 100) / 100;
            preparedStatement.setDouble(1, orderPrice);
            preparedStatement.setInt(2, order);
            preparedStatement.addBatch();
            inserted++;
            total++;
            if(inserted % 100 == 0) {
                System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
                preparedStatement.executeBatch();
                inserted = 0;
            }
        }
        long endTime = System.currentTimeMillis();
        float time = (endTime - startTime) / 1000F;
        if(inserted > 0) {
            System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
            preparedStatement.executeBatch();
        }
        System.out.println("OrderPrice seeder finished successfully after " + time + "s.");
        preparedStatement.close();
    }
}
