package database;

import com.github.javafaker.Faker;
import database.seeders.*;
import java.sql.Connection;
import java.util.Locale;

public class Seeder {
    public static void main(String[] args) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();
            Faker faker = new Faker();
            CustomersSeeder.run(connection,1000, faker);
            OrdersSeeder.run(connection, 1000);
            PublishersSeeder.run(connection, 1000, faker);
            BooksSeeder.run(connection, 100000, faker);
            OrderBookSeeder.run(connection);
            AuthorsSeeder.run(connection, 100000, faker);
            AuthorBookSeeder.run(connection);
            GenresSeeder.run(connection, 1000, faker);
            BookGenreSeeder.run(connection);
            OrderPriceSeeder.run(connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
