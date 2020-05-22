package database;


import org.hibernate.SessionFactory;
import utils.Configuration;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection connection;
    private static Database _instance = null;
    private static SessionFactory sessionFactory;

    public static Connection getConnection() {
        return connection;
    }

    private Database() throws SQLException, ClassNotFoundException {

        connection = DriverManager
                .getConnection(Configuration.getDatabaseUrl(), Configuration.getDatabaseUser(),
                        Configuration.getDatabasePassword());
        Class.forName("org.postgresql.Driver");
        buildSessionFactory();
    }

    /**
     * Return sessionFactory for hibernate
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new Database();
        return _instance;
    }

    /**
     * Execute query on database
     * @param query
     * @throws SQLException
     */
    public static void executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    /**
     * Return number of rows in table
     * @param tableName
     * @return count of rows
     * @throws SQLException
     */
    public static int getRowsCount(String tableName) throws SQLException {
        String query = String.format("SELECT count(*) FROM %s", tableName);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count;
    }

    /**
     * Remove everything from table
     * @param tableName
     * @throws SQLException
     */
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

    /**
     * Get all IDs from table
     * @param tableName
     * @return List of IDs
     * @throws SQLException
     */
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

    /**
     * Create new session factory from hibernate.cfg.xml file
     */
    private static void buildSessionFactory() {
        File cfgFile = new File("src\\main\\resources\\hibernate.cfg.xml");
        sessionFactory = new org.hibernate.cfg.Configuration().configure(cfgFile).buildSessionFactory();
    }
}
