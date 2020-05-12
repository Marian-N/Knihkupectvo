package controller;

import database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderBookController {
    private static OrderBookController _instance = null;
    private static Connection connection;

    private OrderBookController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static List<int[]> getOrderContents(int orderID) throws SQLException {
        List<int[]> orderContents = new ArrayList<>();
        String query = String.format("SELECT book_id, quantity FROM order_book WHERE order_id = %d", orderID);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            int[] orderContent = new int[2];
            orderContent[0] = resultSet.getInt("book_id");
            orderContent[1] = resultSet.getInt("quantity");
            orderContents.add(orderContent);
        }
        statement.close();
        resultSet.close();
        return orderContents;
    }

    public double getTotalPrice(int orderID) throws SQLException {
        String query = String.format(
                "SELECT SUM(ob.quantity * b.price) as total_price " +
                "FROM orders o " +
                "JOIN order_book ob on ob.order_id = o.id " +
                "JOIN books b on b.id = ob.book_id " +
                "WHERE o.id = %s;", orderID);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        double totalPrice = resultSet.getInt(1);
        resultSet.close();
        statement.close();

        return totalPrice;
    }

    public static OrderBookController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new OrderBookController();
        return _instance;
    }
}
