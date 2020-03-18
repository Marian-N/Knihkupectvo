package database;

import com.github.javafaker.Faker;
import database.seeders.CustomersSeeder;
import database.seeders.PublishersSeeder;
import utils.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;

public class Seeder {
    public static void main(String[] args) {
        Configuration configuration = Configuration.getInstance();
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                         .getConnection(configuration.databaseUrl, configuration.databaseUser,
                                 configuration.databasePassword);
            Faker faker = new Faker(new Locale("sk_SK"));
            PublishersSeeder.run(connection, 100, faker);
            CustomersSeeder.run(connection,100, faker);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
