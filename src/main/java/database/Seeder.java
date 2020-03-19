package database;

import com.github.javafaker.Faker;
import database.seeders.*;
import utils.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;

public class Seeder {
    public static void main(String[] args) {
        Connection connection;
        Configuration configuration = Configuration.getInstance();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                         .getConnection(Configuration.databaseUrl, Configuration.databaseUser,
                                 Configuration.databasePassword);
            Faker faker = new Faker(new Locale("sk_SK"));
            CustomersSeeder.run(connection,200, faker);
            OrdersSeeder.run(connection, 150);
            PublishersSeeder.run(connection, 100, faker);
            BooksSeeder.run(connection, 200, faker);
            OrderBookSeeder.run(connection);
            AuthorsSeeder.run(connection, 150, faker);
            AuthorBook.run(connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
