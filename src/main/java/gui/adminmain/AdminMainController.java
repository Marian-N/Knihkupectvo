package gui.adminmain;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import controller.*;
import gui.adminchangebook.ChangeBookController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import model.Author;
import model.Book;
import model.Genre;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    private TableColumn<Book, List<String>> genreColumn;
    @FXML
    private TableColumn<Book, List<String>> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Date> yearColumn;

    private BooksController booksController = BooksController.getInstance();
    private AuthorBookController authorBookController = AuthorBookController.getInstance();
    private AuthorsController authorsController = AuthorsController.getInstance();
    private BookGenreController bookGenreController = BookGenreController.getInstance();
    private GenresController genresController = GenresController.getInstance();

    ObservableMap<Integer, Book> booksFromMap = FXCollections.observableHashMap();
    ObservableMap<Integer, Author> authorsFromMap = FXCollections.observableHashMap();
    ObservableMap<Integer, Genre> genresFromMap = FXCollections.observableHashMap();


    public AdminMainController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        booksFromMap = booksController.getBooks();
        ObservableList<Book> books = FXCollections.observableArrayList(booksFromMap.values());

        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisherColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new SimpleStringProperty(param.getValue().getPublisher().getName());
            }
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        authorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, List<String>>, ObservableValue<List<String>>>() {
            @Override
            public ObservableValue<List<String>> call(TableColumn.CellDataFeatures<Book, List<String>> param) {
                List<Integer> authorId = null;
                try {
                    authorId = authorBookController.getAuthors(param.getValue().getID());
                } catch (SQLException e) {
                }
                //all authors with their ids as keys
                authorsFromMap = authorsController.getAuthors();
                List<String> authorName = new ArrayList<>();
                //taking only names from hashmap
                for (Integer id : authorId){
                    authorName.add(authorsFromMap.get(id).getName());
                }
                return new SimpleObjectProperty(String.join(", ", authorName));
            }
        });
        genreColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, List<String>>, ObservableValue<List<String>>>() {
            @Override
            public ObservableValue<List<String>> call(TableColumn.CellDataFeatures<Book, List<String>> param) {
                List<Integer> genreId = null;
                try {
                    genreId = bookGenreController.getGenres(param.getValue().getID());
                } catch (SQLException e) {
                }
                //all genres with their ids as keys
                genresFromMap = genresController.getGenres();
                List<String> genreName = new ArrayList<>();
                //taking only names from hashmap
                for (Integer id : genreId){
                    genreName.add(genresFromMap.get(id).getName());
                }
                return new SimpleObjectProperty(String.join(", ", genreName));
            }
        });
        bookOverviewTable.setItems(books);

    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void handleBookSelected(MouseEvent mouseEvent) throws SQLException {
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();
        //all authors ids
        List<Integer> authorId = authorBookController.getAuthors(book.getID());
        //all authors with their ids as keys
        authorsFromMap = authorsController.getAuthors();
        List<String> authorName = new ArrayList<>();
        //taking only names from hashmap
        for (Integer id : authorId){
            authorName.add(authorsFromMap.get(id).getName());
        }
        if(book != null){
            synopsisText.setText("Authors: ");
            synopsisText.appendText(String.join(", ", authorName));
        }

        if(book != null){
            synopsisText.appendText("\n\nSynopsis\n\n");
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
