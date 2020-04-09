package database;


import utils.Configuration;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection connection;
    private static Database _instance = null;

    public static Connection getConnection() {
        return connection;
    }

    private Database() throws SQLException, ClassNotFoundException {

        connection = DriverManager
                .getConnection(Configuration.getDatabaseUrl(), Configuration.getDatabaseUser(),
                        Configuration.getDatabasePassword());
        Class.forName("org.postgresql.Driver");
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new Database();
        return _instance;
    }

    public static int getRowsCount(String tableName) throws SQLException {
        String query = String.format("SELECT count(*) FROM %s", tableName);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count;
    }

    public static double getOrderPrice(int ID) throws SQLException {
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

    public static void emptyTable(String tableName) throws SQLException {
        String query = String.format("TRUNCATE %s RESTART IDENTITY CASCADE;", tableName);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Failed to empty" + tableName + "table.\n");
        }
        finally {
            if(statement != null) statement.close();
        }
    }

    public static List<Integer> getTableIDs(String tableName) throws SQLException {
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
