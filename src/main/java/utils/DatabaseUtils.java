package utils;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    public static int getRowsCount(Connection connection, String tableName) throws SQLException {
        String query = String.format("SELECT count(*) FROM %s", tableName);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt(1);
    }

    public static double getOrderPrice(Connection connection, int ID) throws SQLException {
        ArrayList<Integer> IDs = new ArrayList<>();
        String query = String.format("SELECT book_id FROM order_book WHERE order_id=%s", ID);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("id"));
        }
        System.out.println(IDs);
        resultSet.close();
        statement.close();
        return 0.2;
    }

    public static void emptyTable(Connection connection, String tableName) throws SQLException {
        String query = String.format("TRUNCATE %s RESTART IDENTITY CASCADE;", tableName);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }

    public static List<Integer> getTableIDs(Connection connection, String tableName) throws SQLException {
        List<Integer> IDs = new ArrayList<>();
        String query = String.format("SELECT id FROM %s", tableName);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("id"));
        }
        resultSet.close();
        statement.close();
        return IDs;
    }
}
