package gui.usermain;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controller.*;
import gui.ScreenConfiguration;
import gui.usermain.confirmation.CancelConfirmation;
import gui.usermain.makeorder.MakeOrderController;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.io.IOException;
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
    private JFXButton addToOrderButton;
    @FXML
    private JFXButton orderBooksButton;
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
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    @FXML
    private JFXTextField userIdTextField;
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


    private BooksController booksController = BooksController.getInstance();
    private AuthorBookController authorBookController = AuthorBookController.getInstance();
    private AuthorsController authorsController = AuthorsController.getInstance();
    private BookGenreController bookGenreController = BookGenreController.getInstance();
    private GenresController genresController = GenresController.getInstance();
    private OrdersController ordersController = OrdersController.getInstance();
    MakeOrderController makeOrderController = new MakeOrderController();

    ObservableList<Book> books = FXCollections.observableArrayList();
    ObservableMap<Integer, Author> authorsFromMap = FXCollections.observableHashMap();
    ObservableMap<Integer, Genre> genresFromMap = FXCollections.observableHashMap();
    ObservableList<Order> orders = FXCollections.observableArrayList();
    ObservableList<OrderContent> newOrder = FXCollections.observableArrayList();


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

        addToOrderButton.setOnAction(e->{
            if(bookOverviewTable.getSelectionModel().getSelectedItem() != null &&
               bookOverviewTable.getSelectionModel().getSelectedItem().getStockQuantity() > 0){
                newOrder.add(handleAddToOrderBook());
            }
        });
        orderBooksButton.setOnAction(e-> {
            try {
                newOrder = handleOrderBooks(newOrder);
                bookOverviewTable.refresh();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    public void deleteNewOrder(){
        System.out.println(newOrder);
        newOrder.removeAll();
    }

    public void initData(int userId){
        userIdTextField.setText(String.valueOf(userId));
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
                "Date - desc",
                "Popularity - asc",
                "Popularity - desc"
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
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
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
            else if(orderBy.equals("Popularity - asc")){
                books = booksController.getBooks(pageNum, "popularity");
            }
            else if(orderBy.equals("Popularity - desc")){
                books = booksController.getBooks(pageNum, "popularity", true);
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



    public void handleBookSelected(MouseEvent mouseEvent){
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();

        if(book != null){
            synopsisText.setText("Synopsis\n\n");
            synopsisText.appendText(book.getDescription());
        }
    }

    public ObservableList<OrderContent> handleOrderBooks(ObservableList<OrderContent> newOrder) throws IOException {
        ScreenConfiguration screenConfiguration = new ScreenConfiguration();
        int userId = Integer.parseInt(userIdTextField.getText());
        newOrder = screenConfiguration.setMakeOrderScene(newOrder, userId);
        return newOrder;
    }

    public OrderContent handleAddToOrderBook() {
        Book chosenBook = bookOverviewTable.getSelectionModel().getSelectedItem();
        OrderContent newOrder = new OrderContent(chosenBook, 1);
        return newOrder;
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

    //Orders history controllers
    public void handleOrdersHistorySelected(Event event) throws SQLException, ClassNotFoundException {
        createOrderTable();
        createOrderDetailTable();
        int customerID= Integer.parseInt(userIdTextField.getText());
        orders = ordersController.getOrders(customerID);
        orderOverviewTable.setItems(orders);
    }


    public void createOrderTable(){
        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        orderPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
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


    public void handleOrderSelected(MouseEvent mouseEvent) {
        //fill detail table
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();

        if(order != null){
            ObservableList<OrderContent> orderContent =  order.getOrderContents();
            orderDetailTable.setItems(orderContent);
        }
    }


    public void handleOrderCancel(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();
        if (order != null && order.getStatus().equals("nevybavená")){
            CancelConfirmation cancelConfirmation = new CancelConfirmation();
            ScreenConfiguration screenConfiguration = new ScreenConfiguration();
            screenConfiguration.setCancelConfirmationScene(order);
            orderOverviewTable.refresh();
            bookOverviewTable.refresh();
        }
    }

    public void handleLogout(javafx.event.ActionEvent event) throws IOException {
        ScreenConfiguration screenConfiguration = new ScreenConfiguration();
        screenConfiguration.setLoginScene(event);
    }
    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

}
