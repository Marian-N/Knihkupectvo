package database;

import utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class Seeder {
    public static void main(String[] args) {
        Configuration configuration = Configuration.getInstance();
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                         .getConnection(configuration.databaseUrl, configuration.databaseUser,
                                 configuration.databasePassword);
        } catch (Exception e) {
            System.out.println("Could not connect to database.");
        }
    }
}
