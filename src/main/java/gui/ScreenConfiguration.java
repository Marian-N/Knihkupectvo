package gui;

import gui.adminchangebook.ChangeBookController;
import gui.adminmain.AdminMainController;
import gui.usermain.UserMainController;
import gui.usermain.confirmation.CancelConfirmation;
import gui.usermain.makeorder.MakeOrderController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;
import model.Order;
import model.OrderContent;

import java.io.IOException;


public class ScreenConfiguration {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 900, 500));
        primaryStage.show();
    }

    // change scene to user
    public void setMainUserScene(javafx.event.ActionEvent event, int userId) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/usermain/user_main.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene((Pane) loader.load()));

        UserMainController userMainController = loader.<UserMainController>getController();
        userMainController.initData(userId);

        stage.setResizable(false);
        stage.show();
    }


    public void setChangeBookScene(Book book) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/adminchangebook/change_book.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        ChangeBookController changeBookController = loader.<ChangeBookController>getController();
        changeBookController.initData(book);
        stage.setResizable(false);
        stage.setTitle("Change book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        return ;
    }

    public void setCancelConfirmationScene(Order order) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/usermain/confirmation/cancel_confirmation.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        CancelConfirmation cancelConfirmation = loader.<CancelConfirmation>getController();
        cancelConfirmation.cancelConfirm(order, stage);
        //cancelConfirmation.initData(book);

        stage.setResizable(false);
        stage.setTitle("Cancel order");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return;
    }

    public void setLoginScene(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login/login.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene((Pane) loader.load(), 900, 500));
        stage.setResizable(false);
        stage.show();
    }

    public ObservableList<OrderContent> setMakeOrderScene(ObservableList<OrderContent> newOrder, int userId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/usermain/makeorder/make_order.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        MakeOrderController makeOrderController = loader.<MakeOrderController>getController();
        makeOrderController.initData(newOrder, userId, stage);
        stage.setResizable(false);
        stage.setTitle("Complete order");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return makeOrderController.getNewOrder();
    }

}
