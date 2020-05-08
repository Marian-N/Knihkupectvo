package gui.login;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import gui.ScreenConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Customer;
import security.Login;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private JFXTextField nameLogin;
    @FXML
    private JFXPasswordField passwordLogin;
    @FXML
    private Label wrongInfo;

    ScreenConfiguration screenConfiguration = new ScreenConfiguration();
    Customer user;


    @FXML
    private void handleLogin(javafx.event.ActionEvent event) throws Exception {

        user = Login.verify(nameLogin.getText(), passwordLogin.getText());

        if(user != null){
            wrongInfo.setVisible(false);
            if(user.getRole() == 1){
                loadMainAdminScene(event);
            }
            else{
                loadMainUserScene(event, user);
            }
        }
        else{
            nameLogin.clear();
            passwordLogin.clear();
            wrongInfo.setVisible(true);
        }
    }

    //change scene to admin
    private void loadMainAdminScene(javafx.event.ActionEvent event) throws Exception {
        screenConfiguration.setMainAdminScene(event);
    }

    // change scene to user
    private void loadMainUserScene(javafx.event.ActionEvent event, Customer customer) throws Exception {
        screenConfiguration.setMainUserScene(event, customer);
    }
}
