package database.seeders;

import controller.BooksController;
import controller.OrderBookController;
import database.Database;
import javafx.collections.ObservableMap;
import model.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class OrderPriceSeeder {
    public static void run(Connection connection) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        ObservableMap<Integer, Book> books = BooksController.getInstance().getAllBooks();
        List<Integer> orders = database.getTableIDs("orders");
        double orderPrice = 0;
        for(int order : orders){
            orderPrice = 0;
            List<int[]> orderContents = OrderBookController.getInstance().getOrderContents(order);
            for(int[] orderContent : orderContents){
                double bookPrice = books.get(orderContent[0]).getPrice();
                int bookQuantity = orderContent[1];
                orderPrice += bookPrice * bookQuantity;
            }
            orderPrice = (double) Math.round(orderPrice * 100) / 100;
            String query = String.format("UPDATE orders SET price = %s WHERE id = %s", orderPrice, order);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        }
    }
}