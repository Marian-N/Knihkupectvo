package controller;

import database.Database;
import model.Customer;

import java.sql.*;

public class CustomerController {
    private static CustomerController instance = null;
    private Connection connection;

    private CustomerController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static CustomerController getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null) instance = new CustomerController();
        return instance;
    }

    public Customer getCustomerFromRS(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("customer_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String mail = resultSet.getString("mail");
        String city = resultSet.getString("city");
        String zip = resultSet.getString("zip");
        String address = resultSet.getString("address");
        return new Customer(id, firstName, lastName, mail, city, zip, address);
    }

    public Customer getCustomer(int id) throws SQLException {
        String query = "SELECT * FROM customers WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        Customer customer = new Customer(resultSet);
        statement.close();
        return customer;
    }

    public String getBestCustomers(int count) throws SQLException {
        String query = "SELECT c.id FROM customers c " +
                "JOIN orders o ON c.id=o.customer_id " +
                "WHERE o.status='vybavená' " +
                "GROUP BY c.id " +
                "HAVING COUNT(c.id)>=5 " +
                "ORDER BY COUNT(c.id) DESC " +
                "LIMIT ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, count);
        ResultSet resultSet = preparedStatement.executeQuery();
        String customersIDs = "";
        while(resultSet.next()){
            if(customersIDs.equals("")){
                customersIDs +=resultSet.getString(1);
            }
            else{
                customersIDs += ", " + resultSet.getString(1);
            }
        }
        resultSet.close();
        preparedStatement.close();
        return customersIDs;
    }
}
