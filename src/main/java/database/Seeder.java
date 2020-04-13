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
            CustomersSeeder.run(connection,200, faker);
            OrdersSeeder.run(connection, 10000);
            PublishersSeeder.run(connection, 100, faker);
            BooksSeeder.run(connection, 10000, faker);
            OrderBookSeeder.run(connection);
            AuthorsSeeder.run(connection, 500, faker);
            AuthorBookSeeder.run(connection);
            GenresSeeder.run(connection, 150, faker);
            BookGenreSeeder.run(connection);
            OrderPriceSeeder.run(connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
