package gui.adminchangebook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.Global;
import model.Book;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ChangeBookController {
    @FXML
    private JFXTextField changePriceTextField;
    @FXML
    private JFXTextField changeStockTextField;
    @FXML
    private JFXTextField bookTitle;
    @FXML
    private JFXTextField bookId;
    @FXML
    private JFXTextField bookPrice;
    @FXML
    private JFXTextField bookStock;
    @FXML
    private JFXButton changePriceButton;
    @FXML
    private JFXButton changeStockButton;

    void initialize(){}

    public void initData(Book book){
        bookTitle.setText(String.valueOf(book.getTitle()));
        bookId.setText(String.valueOf(book.getID()));
        bookPrice.setText(String.valueOf(book.getPrice()));
        bookStock.setText(String.valueOf(book.getStockQuantity()));
        changePriceButton.setOnAction(e->handleChangePrice(e, book));
        changeStockButton.setOnAction(e->handleChangeStock(e, book));

    }

    public void handleChangePrice(ActionEvent event, Book book) {
        System.out.println(book.getTitle());
    }

    public void handleChangeStock(ActionEvent event, Book book) {
    }
}
