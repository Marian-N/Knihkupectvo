package gui.usermain;

import application.FxmlView;
import application.StageManager;
import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controller.*;
import database.Database;
import application.ScreenConfiguration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import utils.LanguageResource;
import utils.PdfGenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
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
    private TextField userIdTextField;
    @FXML
    private TableView<Order> orderOverviewTable;
    @FXML
    private TableColumn<Order, Integer> orderIDColumn;
    @FXML
    private TableColumn<Order, Integer> orderPriceColumn;
    @FXML
    private TableColumn<Order, Date> orderDateColumn;
    @FXML
    private TableColumn<Order, String> orderStatusColumn;
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
    private OrdersController ordersController = OrdersController.getInstance();

    ObservableList<Book> books = FXCollections.observableArrayList();
    ObservableList<Order> orders = FXCollections.observableArrayList();
    ObservableList<OrderContent> newOrder = FXCollections.observableArrayList();
    Customer loggedUser;
    private String searchingForBook = null;
    private final StageManager stageManager;
    private LanguageResource lr = LanguageResource.getInstance();

    @Autowired
    @Lazy
    public UserMainController(StageManager stageManager) throws SQLException, ClassNotFoundException {
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBooksTable();
        createOrderByBooksComboBox();

        try {
            paginationBooks.setPageCount((int) Math.ceil((double) Database.getRowsCount("books")/100));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        paginationBooks.setPageFactory(this::createBooksPage);
        //adds to order selected book
        addToOrderButton.setOnAction(e->{
            if(bookOverviewTable.getSelectionModel().getSelectedItem() != null &&
               bookOverviewTable.getSelectionModel().getSelectedItem().getStockQuantity() > 0){
                newOrder.add(handleAddToOrderBook());
            }
        });
        //goes to order book scene
        orderBooksButton.setOnAction(e-> {
            try {
                newOrder = handleOrderBooks(newOrder);
                bookOverviewTable.refresh();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        //listens to text in search book - Title of book written - and when its null, refreshes table with all books
        searchBookText.textProperty().addListener((v, oldText, newText) -> {
            if (newText.isEmpty()){
                searchingForBook = null;
                createBooksPage(0);
                try {
                    paginationBooks.setPageCount((int) Math.ceil((double) Database.getRowsCount("books")/100));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Initialize needed data for scene - Logged user - customer
     * @param gotUser - logged user
     */
    public void initData(Customer gotUser){
        loggedUser = gotUser;
        userIdTextField.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName() + "   ID: " + loggedUser.getID());
    }
    /**
     * In Books tab, creates combobox, containing possible sorting of books in books table
     * From LanguageResource takes sorting possibilities in different languages
     * Differs from Admins combobox -> contains sorting by popularity
     */
    private void createOrderByBooksComboBox() {
        orderByBooksComboBox.getItems().addAll(
                "-----",
                lr.getResources().getString("sort_by_title_asc"),       //"Book name - asc",
                lr.getResources().getString("sort_by_title_desc"),      //"Book name - desc",
                lr.getResources().getString("sort_by_author_asc"),      //"Author - asc",
                lr.getResources().getString("sort_by_author_desc"),     //"Author - desc",
                lr.getResources().getString("sort_by_price_asc"),       //"Price - asc",
                lr.getResources().getString("sort_by_price_desc"),      //"Price - desc",
                lr.getResources().getString("sort_by_date_asc"),        //"Date - asc",
                lr.getResources().getString("sort_by_date_desc"),       //"Date - desc",
                lr.getResources().getString("sort_by_popularity_asc"),  //"Popularity - asc",
                lr.getResources().getString("sort_by_popularity_desc")  //"Popularity - desc"
        );
        orderByBooksComboBox.setValue("-----");
    }

    public void createBooksTable(){
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisherColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPublisher().getName()));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        genreColumn.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getGenres().getStringGenres()));
        authorColumn.setCellValueFactory(param -> {
            List<Integer> authorId = null;
            try {
                authorId = authorBookController.getAuthors(param.getValue().getID());
            } catch (SQLException e) {
                return new SimpleObjectProperty("-");
            }

            //all authors with their ids as keys
            List<String> authorName = new ArrayList<>();

            //taking only names from hashmap
            for (Integer id : authorId){
                authorName.add(authorsController.getAuthor(id).getName());
            }
            return new SimpleObjectProperty(String.join(", ", authorName));
        });
    }

    /**
     * Creates corresponding page of books with correct sorting, taken from orderByBooksComboBox
     * @param pageNum wanted page in books table, books tab
     * @return page in books table
     */
    private Node createBooksPage(int pageNum){
        String orderBy = orderByBooksComboBox.getValue();
        try {
            if(searchingForBook != null){
                books = (ObservableList<Book>) booksController.findBook(searchingForBook, pageNum)[1];
            }
            else {
                if (orderBy == null) {
                    books = booksController.getBooks(pageNum, "id");
                } else if (orderBy.equals(lr.getResources().getString("sort_by_title_asc"))) {
                    books = booksController.getBooks(pageNum, "title");
                } else if (orderBy.equals(lr.getResources().getString("sort_by_title_desc"))) {
                    books = booksController.getBooks(pageNum, "title", true);
                } else if (orderBy.equals(lr.getResources().getString("sort_by_author_asc"))) {
                    books = booksController.getBooks(pageNum, "author");
                } else if (orderBy.equals(lr.getResources().getString("sort_by_author_desc"))) {
                    books = booksController.getBooks(pageNum, "author", true);
                } else if (orderBy.equals(lr.getResources().getString("sort_by_price_asc"))) {
                    books = booksController.getBooks(pageNum, "price");
                } else if (orderBy.equals(lr.getResources().getString("sort_by_price_desc"))) {
                    books = booksController.getBooks(pageNum, "price", true);
                } else if (orderBy.equals(lr.getResources().getString("sort_by_date_asc"))) {
                    books = booksController.getBooks(pageNum, "publication_date");
                } else if (orderBy.equals(lr.getResources().getString("sort_by_date_desc"))) {
                    books = booksController.getBooks(pageNum, "publication_date", true);
                } else if (orderBy.equals(lr.getResources().getString("sort_by_popularity_asc"))) {
                    books = booksController.getBooks(pageNum, "popularity");
                } else if (orderBy.equals(lr.getResources().getString("sort_by_popularity_desc"))) {
                    books = booksController.getBooks(pageNum, "popularity", true);
                } else {
                    books = booksController.getBooks(pageNum, "id");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            books = null;
        }
        bookOverviewTable.setItems(books);
        return bookOverviewTable;
    }

    /**
     * When book is selected - shows its description
     */
    public void handleBookSelected(){
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();

        if(book != null){
            synopsisText.setText(book.getDescription());
        }
    }

    /**
     * Calls stage of completing order, gets created order from it
     * @param newOrder list containing all books, which user wants to order
     * @return created order
     * @throws IOException
     */
    public ObservableList<OrderContent> handleOrderBooks(ObservableList<OrderContent> newOrder) throws IOException {
        ScreenConfiguration screenConfiguration = new ScreenConfiguration();
        int userId = loggedUser.getID();
        newOrder = screenConfiguration.setMakeOrderScene(newOrder, userId);
        return newOrder;
    }

    /**
     * takes chosen book from table and returns it
     * @return book to be added to list containing all books, which user wants to order
     */
    public OrderContent handleAddToOrderBook() {
        Book chosenBook = bookOverviewTable.getSelectionModel().getSelectedItem();
        OrderContent newOrder = new OrderContent(chosenBook, 1);
        return newOrder;
    }

    public void handleBookOrderChange() {
        createBooksPage(paginationBooks.getCurrentPageIndex());
    }

    /**
     * when valid number is entered, goes to page.
     * When searching for books, corresponding number of pages are available for choosing
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void handleGoToPage() throws SQLException, ClassNotFoundException {
        int pageId;
        try{
            pageId =Integer.parseInt(setPageBooksTextField.getText());
        } catch (NumberFormatException e){
            pageId = 0;
        }
        if(searchingForBook != null){
            //total pages when searching for book is double all found books / 100 => 100 pre page
            if(pageId > 0 && pageId <= (int) Math.ceil((int) booksController.findBook(searchingForBook, 0)[0]/100.0)){
                paginationBooks.setCurrentPageIndex(pageId - 1);
            }
        } else {
            //total pages = all books / 100 => 100 books per page. rounds up
            if (pageId > 0 && pageId <= (int) Math.ceil((double) Database.getRowsCount("books") / 100)) {
                paginationBooks.setCurrentPageIndex(pageId - 1);
            }
        }
        createBooksPage(paginationBooks.getCurrentPageIndex());
    }

    /**
     * searching books by title
     * Changes paging, total page number, goes to 1.page
     */
    public void handleSearchBook(){
        String bookToFind = searchBookText.getText();
        if (bookToFind.equals("")){
            createBooksPage(paginationBooks.getCurrentPageIndex());
        }
        else{
            try {
                bookOverviewTable.setItems((ObservableList<Book>) booksController.findBook(bookToFind, 0)[1]);
                searchingForBook = bookToFind;
                paginationBooks.setPageCount((int)Math.ceil((int)booksController.findBook(searchingForBook, 0)[0]/100.0));
                paginationBooks.setCurrentPageIndex(0);
            } catch (SQLException | ClassNotFoundException ignored) {
            }
        }
    }

    /**
     * Initialization of History of orders Tab
     * Creates table of orders, table of order details for logged user
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void handleOrdersHistorySelected() throws SQLException, ClassNotFoundException {
        createOrderTable();
        createOrderDetailTable();
        int customerID= loggedUser.getID();
        orders = ordersController.getOrders(customerID);
        orderOverviewTable.setItems(orders);
    }

    /**
     * Binds values to table, order status has corresponding language
     */
    public void createOrderTable(){
        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        orderPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderStatusColumn.setCellValueFactory(param -> {
            String status = param.getValue().getStatus();
            if("nevybavená".equals(status)){
                status = lr.getResources().getString("order_pending_status");
            } else if("vybavená".equals(status)){
                status = lr.getResources().getString("order_completed_status");
            } else if("zamietnutá".equals(status)){
                status = lr.getResources().getString("order_rejected_status");
            } else if("zrušená".equals(status)){
                status = lr.getResources().getString("order_canceled_status");
            }
            return new SimpleStringProperty(status);
        });
    }

    public void createOrderDetailTable(){
        orderDetailBookColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBook().getTitle()));
        orderDetailQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailPriceColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getBook().getPrice())));
    }

    /**
     * Fills Order details table with data from chosen Order
     */
    public void handleOrderSelected() {
        //fill detail table
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();

        if(order != null){
            ObservableList<OrderContent> orderContent =  order.getOrderContents();
            orderDetailTable.setItems(orderContent);
        }
    }

    /**
     * Changes status of chosen order to cancelled, if order was still pending
     * @throws IOException
     */
    public void handleOrderCancel() throws IOException {
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();
        if (order != null && order.getStatus().equals("nevybavená")){
            ScreenConfiguration screenConfiguration = new ScreenConfiguration();
            screenConfiguration.setCancelConfirmationScene(order);
            orderOverviewTable.refresh();
            bookOverviewTable.refresh();
        }
    }

    public void handleLogout(javafx.event.ActionEvent event) throws IOException {
        stageManager.switchScene(FxmlView.LOGIN);
    }
    public void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Creates PDF of order details of chosen order
     * @throws IOException
     * @throws DocumentException
     */
    public void handleGetPDF() throws IOException, DocumentException {
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();
        if (order != null){
            PdfGenerator.generateOrderPDF(order);
        }
    }
}
