package database;

import com.github.javafaker.Faker;
import database.seeders.*;
import java.sql.Connection;

public class Seeder {
    public static void main(String[] args) {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConnection();
            Faker faker = new Faker();
            long startTime = System.currentTimeMillis();
            System.out.println("Starting seeders.");
            CustomersSeeder.run(connection,1000, faker);
            OrdersSeeder.run(connection, 1500);
            PublishersSeeder.run(connection, 50, faker);
            BooksSeeder.run(connection, 1000, faker);
            OrderBookSeeder.run(connection);
            AuthorsSeeder.run(connection, 500, faker);
            AuthorBookSeeder.run(connection);
            GenresSeeder.run(connection, 100, faker);
            BookGenreSeeder.run(connection);
            OrderPriceSeeder.run(connection);
            long endTime = System.currentTimeMillis();
            float time = (endTime - startTime) / 1000F;
            System.out.println("All seeders finished after " + time + "s");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
