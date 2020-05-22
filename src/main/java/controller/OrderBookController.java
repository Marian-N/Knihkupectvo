package controller;

import database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderBookController {
    private static OrderBookController _instance = null;
    private static Connection connection;

    private OrderBookController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    /**
     * Get total price(sum of all books*quantity) of order
     * @param orderID
     * @return
     * @throws SQLException
     */
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
