package gui.usermain;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
    private JFXComboBox<String> orderByBooksComboBox;
    @FXML
    private JFXTextField setPageBooksTextField;
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

    ObservableList<Book> books = FXCollections.observableArrayList();
    ObservableMap<Integer, Author> authorsFromMap = FXCollections.observableHashMap();
    ObservableMap<Integer, Genre> genresFromMap = FXCollections.observableHashMap();


    public UserMainController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBooksTable();
        createOrderByBooksComboBox();
        //findBook();
        paginationBooks.setPageCount(1000);
        paginationBooks.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createBooksPage(pageIndex);
            }
        });
    }

    private void createOrderByBooksComboBox() {
        orderByBooksComboBox.getItems().addAll(
                "-----",
                "Book name - asc",
                "Book name - desc",
                "Author - asc",
                "Author - desc",
                "Price - asc",
                "Price - desc",
                "Date - asc",
                "Date - desc"
        );
        orderByBooksComboBox.setValue("-----");
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

//    private void findBook(){
//        //search
//        ObservableList<Book> books = FXCollections.observableArrayList(booksFromMap.values());
//        FilteredList<Book> filteredList = new FilteredList<>(books, e -> true);
//        searchBookText.setOnKeyReleased(e ->{
//            searchBookText.textProperty().addListener((v, oldValue, newValue) -> {
//                filteredList.setPredicate(book ->{
//                    if (newValue == null || newValue.isEmpty()){
//                        return true;
//                    }
//                    String lowerCaseFilter = newValue.toLowerCase();
//                    if (book.getTitle().toLowerCase().contains(newValue)){
//                        return true;
//                    }
//                    return false;
//                });
//            });
//            SortedList<Book> sortList = new SortedList<>(filteredList);
//            sortList.comparatorProperty().bind(bookOverviewTable.comparatorProperty());
//            bookOverviewTable.setItems(sortList);
//        });
//    }

    private Node createBooksPage(int pageNum){
        String orderBy = orderByBooksComboBox.getValue();
        try {
            if(orderBy == null){
                books = booksController.getBooks(pageNum, "id");
            }
            else if(orderBy.equals("Book name - asc")){
                books = booksController.getBooks(pageNum, "title");
            }
            else if(orderBy.equals("Book name - desc")){
                books = booksController.getBooks(pageNum, "title", true);
            }
            else if(orderBy.equals("Author - asc")){
                books = booksController.getBooks(pageNum, "author");
            }
            else if(orderBy.equals("Author - desc")){
                books = booksController.getBooks(pageNum, "author", true);
            }
            else if(orderBy.equals("Price - asc")){
                books = booksController.getBooks(pageNum, "price");
            }
            else if(orderBy.equals("Price - desc")){
                books = booksController.getBooks(pageNum, "price", true);
            }
            else if(orderBy.equals("Date - asc")){
                books = booksController.getBooks(pageNum, "publication_date");
            }
            else if(orderBy.equals("Date - desc")){
                books = booksController.getBooks(pageNum, "publication_date", true);
            }
            else{
                books = booksController.getBooks(pageNum, "id");
            }
        } catch (SQLException e) {
            books = null;
        } catch (ClassNotFoundException e) {
            books = null;
        }
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

    public void handleBookOrderChange(ActionEvent actionEvent) {
        createBooksPage(paginationBooks.getCurrentPageIndex());
    }

    public void handleGoToPage(ActionEvent actionEvent) {
        int pageId = 0;
        try{
            pageId =Integer.parseInt(setPageBooksTextField.getText());
        } catch (NumberFormatException e){
            pageId = 0;
        }

        if(pageId > 0 && pageId <= 1000){
            paginationBooks.setCurrentPageIndex(pageId - 1);
        }
        createBooksPage(paginationBooks.getCurrentPageIndex());
    }

    //Searching of book by title
    public void handleSearchBook(ActionEvent actionEvent){
        String bookToFind = searchBookText.getText();
        if (bookToFind.equals("")){
            createBooksPage(paginationBooks.getCurrentPageIndex());
        }
        else{
            try {
                bookOverviewTable.setItems(booksController.findBook(bookToFind));
            } catch (SQLException e) {
            } catch (ClassNotFoundException e) {
            }
        }
    }
}
