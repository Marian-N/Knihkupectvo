package security;

import controller.CustomerController;
import model.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class Login {
    public static Customer verify(String mail, String password) throws SQLException, ClassNotFoundException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Customer customer;
        try{
            customer = CustomerController.getInstance().getCustomer(mail);
        }
        catch(Exception e) {
            return null;
        }
        if(passwordEncoder.matches(password, customer.getEncryptedPassword()))
            return customer;
        return null;
    }
}
