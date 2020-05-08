package gui.login.userregister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.CustomerController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;

import java.sql.SQLException;

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
            if(!firstNameField.getText().isEmpty()){
                wrongFirstName.setVisible(false);
                counter++;
                newName = firstNameField.getText();
            } else{
                wrongFirstName.setVisible(true);
            }
            if(!lastNameField.getText().isEmpty()){
                wrongLastName.setVisible(false);
                counter++;
                newSurname = lastNameField.getText();
            } else{
                wrongLastName.setVisible(true);
            }
            if(!mailField.getText().isEmpty()){
                wrongMail.setVisible(false);
                counter++;
                newMail = mailField.getText();
            } else{
                wrongMail.setVisible(true);
            }
            if(!cityField.getText().isEmpty()){
                wrongCity.setVisible(false);
                counter++;
                newCity = cityField.getText();
            } else{
                wrongCity.setVisible(true);
            }
            if(!zipField.getText().isEmpty()){
                wrongZip.setVisible(false);
                counter++;
                newZip = zipField.getText();
            } else{
                wrongZip.setVisible(true);
            }
            if(!addressField.getText().isEmpty()){
                wrongAddress.setVisible(false);
                counter++;
                newAddress = addressField.getText();
            } else{
                wrongAddress.setVisible(true);
            }
            if(!passwordField.getText().isEmpty()){
                wrongPassword.setVisible(false);
                counter++;
                newPassword = passwordField.getText();
            } else{
                wrongPassword.setVisible(true);
            }

            if (counter == 7){
                Customer newCustomer = new Customer(newName, newSurname, newMail, newCity, newZip, newAddress,0, newPassword);
                System.out.println(newCustomer.getFirstName());
                try {
                    customerController.addCustomer(newCustomer);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            stage.close();

        });
    }
}
