package controller;

import database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderBookController {
    private static OrderBookController _instance = null;
    private Database database;
    private static Connection connection;

    public OrderBookController() throws SQLException, ClassNotFoundException {
        database = Database.getInstance();
        connection = database.getConnection();
    }

    public static List<int[]> getOrderContents(int orderID) throws SQLException {
        List<int[]> orderContents = new ArrayList<>();
        String query = String.format("SELECT book_id, quantity FROM order_book WHERE order_id = %d", orderID);
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        while(resultSet.next()) {
            int[] orderContent = new int[2];
            orderContent[0] = resultSet.getInt("book_id");
            orderContent[1] = resultSet.getInt("quantity");
            orderContents.add(orderContent);
        }
        resultSet.close();
        return orderContents;
    }

    public static OrderBookController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new OrderBookController();
        return _instance;
    }
}
