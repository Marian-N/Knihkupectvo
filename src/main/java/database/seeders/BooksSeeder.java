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
        String query = "INSERT INTO books " +
                "(title, price, stock_quantity, publication_year, description, publisher_id)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        DatabaseUtils.emptyTable(connection, "books");
        List<Integer> IDs = DatabaseUtils.getTableIDs(connection, "publishers");
        while(count-- > 0) {
            statement.setString(1, faker.book().title());
            statement.setDouble(2, RandomGenerator.getRandomPrice());
            statement.setInt(3, RandomGenerator.getRandomIntFromInterval(0, 100));
            statement.setDate(4, RandomGenerator.getRandomDate(1920, 2019));
            statement.setString(5, faker.backToTheFuture().quote());
            int randomIndex = RandomGenerator.getRandomIntFromInterval(0, IDs.size() - 1);
            statement.setInt(6, IDs.get(randomIndex));
            statement.executeUpdate();
        }
        statement.close();
    }
}
