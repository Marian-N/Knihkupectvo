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
            Faker faker = new Faker(new Locale("sk_SK"));
            CustomersSeeder.run(connection,200, faker);
            OrdersSeeder.run(connection, 150);
            PublishersSeeder.run(connection, 100, faker);
            BooksSeeder.run(connection, 100000, faker);
            OrderBookSeeder.run(connection);
            AuthorsSeeder.run(connection, 120, faker);
            AuthorBookSeeder.run(connection);
            GenresSeeder.run(connection, 80, faker);
            BookGenreSeeder.run(connection);
            OrderPriceSeeder.run(connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
