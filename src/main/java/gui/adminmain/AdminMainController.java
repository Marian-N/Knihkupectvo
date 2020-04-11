package gui.adminmain;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controller.*;
import database.Database;
import gui.ScreenConfiguration;
import gui.adminchangebook.ChangeBookController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import gui.adminchangebook.ChangeBookController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AdminMainController implements Initializable {

    @FXML
    private JFXButton exitButton;
    @FXML
    private Pagination paginationBooks;
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
    @FXML
    private TableView<Order> orderOverviewTable;
    @FXML
    private TableColumn<Order, Integer> orderIDColumn;
    @FXML
    private TableColumn<Order, String> orderCustomerColumn;
    @FXML
    private TableColumn<Order, Integer> orderPriceColumn;
    @FXML
    private TableColumn<Order, Date> orderDateColumn;
    @FXML
    private TableColumn<Order, String> orderStatusColumn;
    @FXML
    private TableColumn<Order, String> orderCustomerIDColumn;
    @FXML
    private TableView<OrderContent> orderDetailTable;
    @FXML
    private TableColumn<OrderContent, String> orderDetailBookColumn;
    @FXML
    private TableColumn<OrderContent, Integer> orderDetailQuantityColumn;
    @FXML
    private TableColumn<OrderContent, String> orderDetailPriceColumn;
    @FXML
    private JFXComboBox<String> orderStatusChangeComboBox;
    @FXML
    private JFXTextField searchCustomerTextField;


    private BooksController booksController = BooksController.getInstance();
    private AuthorBookController authorBookController = AuthorBookController.getInstance();
    private AuthorsController authorsController = AuthorsController.getInstance();
    private BookGenreController bookGenreController = BookGenreController.getInstance();
    private GenresController genresController = GenresController.getInstance();
    private OrdersController ordersController = OrdersController.getInstance();

    ObservableList<Book> books = FXCollections.observableArrayList();
    ObservableMap<Integer, Author> authorsFromMap = FXCollections.observableHashMap();
    ObservableMap<Integer, Genre> genresFromMap = FXCollections.observableHashMap();
    ObservableList<Order> orders = FXCollections.observableArrayList();

    ScreenConfiguration screenConfiguration = new ScreenConfiguration();


    public AdminMainController() throws SQLException, ClassNotFoundException {
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
        orderByBooksComboBox.getItems().setAll(
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

    public void handleBookOrderChange(ActionEvent actionEvent) {
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

    public void handleChangeBook(javafx.event.ActionEvent event) throws IOException {
        //ChangeBookController changeBookController = new ChangeBookController();
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();
        if(book != null){
            screenConfiguration.setChangeBookScene(book);
        }
    }

    public void handleDeleteBook(ActionEvent actionEvent) {

    }

    //load orders after searching of customer id
    public void handleLoadOrders(Event event) throws SQLException, ClassNotFoundException {

        int customerID;
        try{
            customerID = Integer.parseInt(searchCustomerTextField.getText());
        }catch(NumberFormatException e){
            customerID = -1;
        }

        orders = ordersController.getOrders(customerID);
        orderOverviewTable.setItems(orders);

    }

    public void createOrderTable(){
        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        orderPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderCustomerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param) {
                return new SimpleStringProperty(param.getValue().getCustomer().getFirstName() + " " +
                        param.getValue().getCustomer().getLastName());

            }
        });
        orderCustomerIDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> param) {
                return new SimpleStringProperty(String.valueOf(param.getValue().getCustomer().getID()));

            }
        });

    }

    public void createOrderDetailTable(){
        orderDetailBookColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderContent, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderContent, String> param) {
                return new SimpleStringProperty(param.getValue().getBook().getTitle());
            }
        });
        orderDetailQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderContent, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderContent, String> param) {
                return new SimpleStringProperty(String.valueOf(param.getValue().getBook().getPrice()));
            }
        });
    }

    public void handleOrderSelected(MouseEvent mouseEvent){
        //fill detail table
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();

        if(order != null){
            ObservableList<OrderContent> orderContent =  order.getOrderContents();
            orderDetailTable.setItems(orderContent);
        }

        //change status of order
        orderStatusChangeComboBox.setValue(" ");
    }

    public void createOrderStatusComboBox(){

        orderStatusChangeComboBox.getItems().setAll(
                "vybavená",
                "zamietnutá",
                "nevybavená"
        );

    }

    public void handleOrderStatusChange(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();
        if (order != null && orderStatusChangeComboBox.getValue() != " "){
            ordersController.changeStatus(order, orderStatusChangeComboBox.getValue());
            orderOverviewTable.refresh();
        }
    }

    public void handleOrdersSetup(Event event) {
        createOrderTable();
        createOrderDetailTable();
        createOrderStatusComboBox();
    }
}
