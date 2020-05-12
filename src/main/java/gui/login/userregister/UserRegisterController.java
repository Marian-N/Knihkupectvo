package gui.login.userregister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.CustomerController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;


import java.sql.SQLException;
import java.util.regex.Pattern;


public class UserRegisterController {

    @FXML
    private JFXButton createButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXTextField firstNameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXTextField mailField;
    @FXML
    private JFXTextField cityField;
    @FXML
    private JFXTextField zipField;
    @FXML
    private JFXTextField addressField;
    @FXML
    private JFXTextField passwordField;
    @FXML
    private Text wrongFirstName;
    @FXML
    private Text wrongLastName;
    @FXML
    private Text wrongMail;
    @FXML
    private Text wrongCity;
    @FXML
    private Text wrongZip;
    @FXML
    private Text wrongAddress;
    @FXML
    private Text wrongPassword;

    CustomerController customerController = CustomerController.getInstance();

    public UserRegisterController() throws SQLException, ClassNotFoundException {
    }

    public void createUser(Stage stage) {
        cancelButton.setOnAction(e -> stage.close());
        createButton.setOnAction(e ->{
            String newName = null;
            String newSurname = null;
            String newMail = null;
            String newCity = null;
            String newZip = null;
            String newAddress = null;
            String newPassword = null;


            int counter = 0;
            if(validateName(firstNameField, wrongFirstName)){
                counter++;
                newName = firstNameField.getText();
            }
            if(validateName(lastNameField, wrongLastName)){
                counter++;
                newSurname = lastNameField.getText();
            }
            if(validateMail(mailField, wrongMail)){
                counter++;
                newMail = mailField.getText();
            }
            if(validateName(cityField, wrongCity)){
                counter++;
                newCity = cityField.getText();
            }
            if(validateZip(zipField, wrongZip)){
                counter++;
                newZip = zipField.getText();
            }
            if(validateName(addressField, wrongAddress)){
                counter++;
                newAddress = addressField.getText();
            }
            if(validatePassword(passwordField, wrongPassword)){
                counter++;
                newPassword = passwordField.getText();
            }

            if (counter == 7){
                Customer newCustomer = new Customer(newName, newSurname, newMail, newCity, newZip, newAddress,0, newPassword);
                try {
                    customerController.addCustomer(newCustomer);
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                stage.close();
            }

        });
    }

    private boolean validatePassword(JFXTextField password, Text wrongPassword) {
        if (password.getText().isEmpty() || password.getText().trim().length() == 0 || password.getText().length() > 60){
            wrongPassword.setVisible(true);
            return false;
        } else{
            wrongPassword.setVisible(false);
            return true;
        }
    }

    private boolean validateMail(JFXTextField mail, Text wrongMail) {
        String mailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern mailPattern = Pattern.compile(mailRegex);
        if(!mailPattern.matcher(mail.getText()).matches()){
            wrongMail.setVisible(true);
            return false;
        } else{
            wrongMail.setVisible(false);
            return true;
        }
    }

    private boolean validateName(JFXTextField name, Text wrongName){
        if (name.getText().isEmpty() || name.getText().trim().length() == 0 || name.getText().length() > 255){
            wrongName.setVisible(true);
            return false;
        } else{
            wrongName.setVisible(false);
            return true;
        }
    }
    private boolean validateZip(JFXTextField zip, Text wrongZip){
        if (zip.getText().isEmpty() || zip.getText().trim().length() == 0 || zip.getText().length() > 15){
            wrongZip.setVisible(true);
            return false;
        } else{
            wrongZip.setVisible(false);
            return true;
        }
    }
}
