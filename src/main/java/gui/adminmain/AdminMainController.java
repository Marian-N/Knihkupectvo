package gui.adminmain;

import controller.BooksController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {

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
    ObservableList<Book> books = FXCollections.observableArrayList();

    public AdminMainController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        books = booksController.getBooks();
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisherID"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        bookOverviewTable.setItems(books);
    }

}
