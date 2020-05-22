package controller;

import database.Database;
import model.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.Encoder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.*;

public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private static CustomerController instance = null;
    private Connection connection;

    private CustomerController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static CustomerController getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null) instance = new CustomerController();
        return instance;
    }

    /**
     * Create customer from result set
     * @param resultSet
     * @return Customer
     * @throws SQLException
     */
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

    /**
     * Get customer from database
     * @param id
     * @return Customer
     * @throws SQLException
     */
    public Customer getCustomer(int id) throws SQLException {
        String query = "SELECT * FROM customers WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        Customer customer = new Customer(resultSet);
        statement.close();
        return customer;
    }

    /**
     * Get id of customers that have at least 5 orders
     * @param count number of customers to return
     * @return String of customer ids separated by ','
     * @throws SQLException
     */
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

    /**
     * Return customer with given mail
     * @param mail
     * @return Customer
     */
    public Customer getCustomer(String mail) {
        Session session = Database.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> rootEntry = query.from(Customer.class);
        query.select(rootEntry);
        query.where(criteriaBuilder.
                equal(rootEntry.get("mail"), mail));
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        Customer customer = null;
        try {
            customer = entityManager.createQuery(query).setMaxResults(20).getResultList().get(0);
        }
        catch(Exception e) {
            return null;
        }
        return customer;
    }

    /**
     * Add customer to database
     * @param customer
     * @return true if customer already does not exists
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean addCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        if(customer != null && getCustomer(customer.getMail()) == null) {
            String first = customer.getFirstName();
            String last = customer.getLastName();
            String mail = customer.getMail();
            String message = String.format("%s %s created account, mail = %s.", first, last, mail);
            logger.info(message);
            Session session = Database.getInstance().getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(customer);
            transaction.commit();
            session.close();
            return true;
        }
        return false;
    }

    /**
     * Change role of user
     * @param mail of customer
     * @param role new role
     * @return true if successful, else false
     */
    public boolean changeRole(String mail, int role) {
        Customer customer = getCustomer(mail);
        if(customer != null) {
            Session session = Database.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            customer.setRole(role);
            session.saveOrUpdate(customer);
            transaction.commit();
            session.close();
            return true;
        }
        return false;
    }

    /**
     * Change password of user
     * @param customer
     * @param newPassword
     * @return true is successful, else false
     */
    public boolean changePassword(Customer customer, String newPassword) {
        if(customer != null) {
            Session session = Database.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            customer.setEncryptedPassword(Encoder.encode(newPassword));
            session.saveOrUpdate(customer);
            transaction.commit();
            session.close();
            return true;
        }
        return false;
    }
}
