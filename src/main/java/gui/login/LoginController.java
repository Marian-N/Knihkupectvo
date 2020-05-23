package gui.login;


import application.FxmlView;
import application.StageManager;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import application.ScreenConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import security.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable{

    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private JFXTextField nameLogin;
    @FXML
    private JFXPasswordField passwordLogin;
    @FXML
    private Label wrongInfo;

    private final StageManager stageManager;

    ScreenConfiguration screenConfiguration = new ScreenConfiguration();
    Customer user;

    @Autowired
    @Lazy
    public LoginController(StageManager stageManager){
        this.stageManager = stageManager;
    }

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
        //screenConfiguration.setMainAdminScene(event);
        stageManager.switchScene(FxmlView.ADMIN);
    }

    // change scene to user
    private void loadMainUserScene(javafx.event.ActionEvent event, Customer customer) throws Exception {
//        screenConfiguration.setMainUserScene(event, customer);

        stageManager.switchScene(FxmlView.USER, customer);
    }

    public void handleCreateUser() throws IOException {
        screenConfiguration.setCreateUser();
    }
}
