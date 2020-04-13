package gui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.OrdersController;
import database.Database;
import gui.ScreenConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Database database = Database.getInstance();

    public LoginController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXTextField nameLogin;
    @FXML
    private JFXPasswordField passwordLogin;
    @FXML
    private Label wrongInfo;

    ScreenConfiguration screenConfiguration = new ScreenConfiguration();



    @FXML
    private void handleLogin(javafx.event.ActionEvent event) throws Exception {

        if(nameLogin.getText().contentEquals("admin")){
            loadMainAdminScene(event);
        }
        else {//if(nameLogin.getText().contentEquals("user")){
            int userId;
            int allUsers = database.getTableIDs("customers").size();
            try{
                userId = Integer.parseInt(nameLogin.getText());
            } catch (NumberFormatException e){
                userId = -1;
            }
            if(userId > 0 && userId <= allUsers){
                loadMainUserScene(event, userId);
            }
        }


//        else{
//            nameLogin.clear();
//            passwordLogin.clear();
//            wrongInfo.setStyle("-fx-opacity: 1");
//            nameLogin.getStyleClass().add("wrong-info");
//            passwordLogin.getStyleClass().add("wrong-info");
//
//        }
    }

    //change scene to admin
    private void loadMainAdminScene(javafx.event.ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/adminmain/admin_main.fxml"));
        Scene mainScene = new Scene(root);
        //get info about stage
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.setResizable(false);
        window.show();
    }

    // change scene to user
    private void loadMainUserScene(javafx.event.ActionEvent event, int userId) throws Exception {
        screenConfiguration.setMainUserScene(event, userId);
    }
}
