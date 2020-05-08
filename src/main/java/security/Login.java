package security;

import controller.CustomerController;
import model.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class Login {
    public static boolean verify(String mail, String password) throws SQLException, ClassNotFoundException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Customer customer;
        try{
            customer = CustomerController.getInstance().getCustomer(mail);
        }
        catch(Exception e) {
            return false;
        }
        return passwordEncoder.matches(password, customer.getEncryptedPassword());
    }
}
