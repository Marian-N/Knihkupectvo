package gui.usermain;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controller.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Author;
import model.Book;
import model.Genre;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserMainController implements Initializable {
    @FXML
    private JFXButton exitButton;
    @FXML
    private JFXButton changeBookButton;
    @FXML
    private JFXButton deleteBookButton;
    @FXML
    private JFXTextArea synopsisText;
    @FXML
    private JFXTextField searchBookText;
    @FXML
    private Pagination paginationBooks;
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


    public UserMainController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBooksTable();
        findBook();
        paginationBooks.setPageCount(1000);
        //paginationBooks.setPageFactory(this::createBooksPage);
        paginationBooks.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createBooksPage(pageIndex);
            }
        });
    }

    public void createBooksTable(){
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
                    return new SimpleObjectProperty("-");
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
                    return new SimpleObjectProperty("-");
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
        //bookOverviewTable.setItems(books);
        //return books; //bookOverviewTable.setItems(books);
    }

    private void findBook(){
        //search
        ObservableList<Book> books = FXCollections.observableArrayList(booksFromMap.values());
        FilteredList<Book> filteredList = new FilteredList<>(books, e -> true);
        searchBookText.setOnKeyReleased(e ->{
            searchBookText.textProperty().addListener((v, oldValue, newValue) -> {
                filteredList.setPredicate(book ->{
                    if (newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (book.getTitle().toLowerCase().contains(newValue)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Book> sortList = new SortedList<>(filteredList);
            sortList.comparatorProperty().bind(bookOverviewTable.comparatorProperty());
            bookOverviewTable.setItems(sortList);
        });
    }

    private Node createBooksPage(int pageNum){
        try {
            booksFromMap = booksController.getBooks(pageNum, "id");
            System.out.println(booksController.getBooks(2, "id").keySet());
            // System.out.println(pageNum);
        } catch (SQLException e) {
            booksFromMap = null;
        } catch (ClassNotFoundException e) {
            booksFromMap = null;
        }

        ObservableList<Book> books = FXCollections.observableArrayList(booksFromMap.values());


        //ObservableList<Book> books = fillTable(paginationBooks.getCurrentPageIndex());

//        int bookNum;
//        try {
//            bookNum = Database.getInstance().getRowsCount("books");
//        } catch (SQLException e) {
//            bookNum = 0;
//        } catch (ClassNotFoundException e) {
//            bookNum = 0;
//        }
//        System.out.println(paginationBooks.getCurrentPageIndex());
//        int startNum = pageNum * bookNum/1000;
//        int endNum = Math.min(startNum + bookNum/1000, bookNum);
//        //System.out.println(books.get(1));
        bookOverviewTable.setItems(books);
        return bookOverviewTable;
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void handleBookSelected(MouseEvent mouseEvent){
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();

        if(book != null){
            synopsisText.setText("Synopsis\n\n");
            synopsisText.appendText(book.getDescription());
        }
    }

    public void handleOrderBook(ActionEvent actionEvent) {
    }
}
