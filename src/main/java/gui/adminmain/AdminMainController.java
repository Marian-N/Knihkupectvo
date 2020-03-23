package gui.adminmain;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import controller.BooksController;
import gui.adminchangebook.ChangeBookController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Book;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {

    @FXML
    private JFXButton exitButton;
    @FXML
    private JFXButton changeBookButton;
    @FXML
    private JFXButton deleteBookButton;
    @FXML
    private JFXTextArea synopsisText;
    @FXML
    private TableView<Book> bookOverviewTable;
    @FXML
    private TableColumn<Book, String> bookNameColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> publisherColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Date> yearColumn;

    private BooksController booksController = BooksController.getInstance();
    ObservableMap<Integer, Book> booksFromMap = FXCollections.observableHashMap();


    public AdminMainController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        booksFromMap = booksController.getBooks();
        ObservableList<Book> books = FXCollections.observableArrayList(booksFromMap.values());

        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisherID"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));

        bookOverviewTable.setItems(books);
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void handleBookSelected(MouseEvent mouseEvent) {
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();
        if(book != null){
            synopsisText.setText("Synopsis\n\n");
            synopsisText.appendText(book.getDescription());
        }

    }

    public void handleSearchBook(ActionEvent actionEvent) {

    }

    public void handleChangeBook(javafx.event.ActionEvent event) throws IOException {

    }

    public void handleDeleteBook(ActionEvent actionEvent) {

    }
}
