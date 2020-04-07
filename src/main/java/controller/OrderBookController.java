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

    public static OrderBookController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new OrderBookController();
        return _instance;
    }
}
