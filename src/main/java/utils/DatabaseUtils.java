package utils;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    static private final DatabaseUtils _instance = new DatabaseUtils();
    public DatabaseUtils(){}

    public static void emptyTable(Connection connection, String table_name) throws SQLException {
        String query = String.format("TRUNCATE %s RESTART IDENTITY CASCADE;", table_name);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }

    public static List getTableIDs(Connection connection, String table_name) throws SQLException {
        List<Integer> IDs = new ArrayList();
        int counter = 0;
        String query = String.format("SELECT id FROM %s", table_name);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            IDs.add(resultSet.getInt("id"));
        }
        resultSet.close();
        statement.close();

        return IDs;
    }

    public static DatabaseUtils getInstance(){
        return _instance;
    }
}
