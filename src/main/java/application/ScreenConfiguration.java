package application;

import gui.adminchangebook.ChangeBookController;
import gui.login.userregister.UserRegisterController;
import gui.usermain.confirmation.CancelConfirmation;
import gui.usermain.makeorder.MakeOrderController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;
import model.Order;
import model.OrderContent;

import java.io.IOException;


public class ScreenConfiguration {

    public void setChangeBookScene(Book book) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.ADMINCHANGEBOOK.getFxmlFile()));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        ChangeBookController changeBookController = loader.<ChangeBookController>getController();
        changeBookController.initData(book);
        stage.setResizable(false);
        stage.setTitle(FxmlView.ADMINCHANGEBOOK.getTitle());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return ;
    }

    public void setCancelConfirmationScene(Order order) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.USERCANCELCONFIRM.getFxmlFile()));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        CancelConfirmation cancelConfirmation = loader.<CancelConfirmation>getController();
        cancelConfirmation.cancelConfirm(order, stage);
        //cancelConfirmation.initData(book);

        stage.setResizable(false);
        stage.setTitle(FxmlView.USERCANCELCONFIRM.getTitle());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return;
    }

    public ObservableList<OrderContent> setMakeOrderScene(ObservableList<OrderContent> newOrder, int userId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.USERMAKEORDER.getFxmlFile()));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        MakeOrderController makeOrderController = loader.<MakeOrderController>getController();
        makeOrderController.initData(newOrder, userId, stage);
        stage.setResizable(false);
        stage.setTitle(FxmlView.USERMAKEORDER.getTitle());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return makeOrderController.getNewOrder();
    }


    public void setCreateUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.USERREGISTER.getFxmlFile()));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        UserRegisterController userRegisterController = loader.<UserRegisterController>getController();
        userRegisterController.createUser(stage);

        stage.setResizable(false);
        stage.setTitle(FxmlView.USERREGISTER.getTitle());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
