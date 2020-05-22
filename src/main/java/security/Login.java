package security;

import controller.CustomerController;
import model.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class Login {

    /**
     * Return user with given mail and password
     * @param mail
     * @param password
     * @return Customer or null if user does not exist
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Customer verify(String mail, String password) throws SQLException, ClassNotFoundException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Customer customer;
        try{
            customer = CustomerController.getInstance().getCustomer(mail);
        }
        catch(Exception e) {
            return null;
        }
        try {
            if (passwordEncoder.matches(password, customer.getEncryptedPassword()))
                return customer;
        }
        catch(Exception e) {
            return null;
        }
        return null;
    }
}
