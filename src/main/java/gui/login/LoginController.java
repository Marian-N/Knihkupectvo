package gui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //tu sa dava, ak chcem nieco spravit pri zacati programu, nampr nacitanie z databazi
        System.out.println("Loading user data...");
    }

    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXTextField nameLogin;
    @FXML
    private JFXPasswordField passwordLogin;
    @FXML
    private Label wrongInfo;


    @FXML
    private void handleLogin(javafx.event.ActionEvent event) throws Exception {

        if(true){
            loadMainAdminScene(event);
        }
        else {
            loadMainUserScene(event);
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

    //zmeni scenu na admin
    private void loadMainAdminScene(javafx.event.ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/adminmain/admin_main.fxml"));
        Scene mainScene = new Scene(root);
        //dostanem informaciu o stage == magia
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.setResizable(false);
        window.show();
    }

    //zavola zmenu sceny na usera
    private void loadMainUserScene(javafx.event.ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main-user.fxml"));
        Scene mainScene = new Scene(root);
        //dostanem informaciu o stage == magia
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.setResizable(false);
        window.show();
    }
}
