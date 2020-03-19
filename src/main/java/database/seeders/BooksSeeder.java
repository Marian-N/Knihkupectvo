package database.seeders;

import com.github.javafaker.Faker;
import utils.DatabaseUtils;
import utils.RandomGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BooksSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        PreparedStatement statement = null;
        RandomGenerator generator = RandomGenerator.getInstance();
        DatabaseUtils databaseUtils = DatabaseUtils.getInstance();
        databaseUtils.emptyTable(connection, "books");
        List<Integer> IDs = databaseUtils.getTableIDs(connection, "publishers");
        for(int i = 0; i < count; i++) {
            statement = connection.prepareStatement("INSERT INTO books " +
                    "(title, price, stock_quantity, publication_year, description, publisher_id) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, faker.book().title());
            statement.setDouble(2, generator.getRandomPrice());
            statement.setInt(3, generator.getRandomIntFromInterval(0, 100));
            statement.setDate(4, generator.getRandomDate(1920, 2019));
            statement.setString(5, faker.backToTheFuture().quote());
            statement.setInt(6, IDs.get(generator.getRandomIntFromInterval(0, IDs.size() - 1)));
            statement.executeUpdate();
        }
        statement.close();
    }
}
