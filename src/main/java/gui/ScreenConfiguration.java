package gui;

import gui.adminchangebook.ChangeBookController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;

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

    public Stage setChangeBookScene(Book book) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/adminchangebook/change_book.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        ChangeBookController changeBookController = loader.<ChangeBookController>getController();
        changeBookController.initData(book);
        stage.setResizable(false);
        stage.setTitle("Change book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        return stage;
    }
}
