package utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtils {
    static private final DatabaseUtils _instance = new DatabaseUtils();
    public DatabaseUtils(){}

    public static void emptyTable(Connection connection, String table_name) throws SQLException {
        String query = String.format("truncate %s restart identity cascade;", table_name);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }

    public static DatabaseUtils getInstance(){
        return _instance;
    }
}
