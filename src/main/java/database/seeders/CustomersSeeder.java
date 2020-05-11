package database.seeders;

import com.github.javafaker.Faker;
import database.Database;
import security.Encoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomersSeeder {
    public static void run(Connection connection, Integer count, Faker faker) throws SQLException {
        String query = "INSERT INTO customers " +
                "(first_name, last_name, mail, city, zip, address, role, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Database.emptyTable("customers");
        int inserted = 0;
        int total = 0;
        int total_count = count;
        long startTime = System.currentTimeMillis();
        System.out.println("Starting customers seeder.");
        String password = Encoder.encode("password");
        while(count-- > 0) {
            statement.setString(1, faker.name().firstName());
            statement.setString(2, faker.name().lastName());
            statement.setString(3, faker.internet().emailAddress());
            statement.setString(4, faker.address().city());
            statement.setString(5, faker.address().zipCode());
            statement.setString(6, faker.address().streetAddress());
            statement.setInt(7, 0);
            statement.setString(8, password);
            //statement.setString(8, Encoder.encode("password")); //use this for salting
            statement.addBatch();
            inserted++;
            total++;
            if(inserted % 100 == 0) {
                System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
                statement.executeBatch();
                inserted = 0;
            }
        }
        statement.setString(1, "admin_name");
        statement.setString(2, "admin_last_name");
        statement.setString(3, "admin");
        statement.setString(4, faker.address().city());
        statement.setString(5, faker.address().zipCode());
        statement.setString(6, faker.address().streetAddress());
        statement.setInt(7,1);
        statement.setString(8, Encoder.encode("admin"));
        statement.addBatch();
        statement.executeBatch();
        if(inserted % 100 > 0)
            System.out.printf("%.2f%c\n", (float) total / total_count * 100, '%');
        long endTime = System.currentTimeMillis();
        float time = (endTime - startTime) / 1000F;
        System.out.println("Customers seeder finished successfully after " + time + "s.");
        statement.close();
    }
}
