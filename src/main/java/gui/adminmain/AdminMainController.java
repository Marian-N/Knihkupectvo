package gui.adminmain;

import application.FxmlView;
import application.StageManager;
import com.jfoenix.controls.*;
import controller.*;
import database.Database;
import application.ScreenConfiguration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import utils.LanguageResource;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class AdminMainController implements Initializable {

    @FXML
    private JFXButton exitButton;
    @FXML
    private Pagination paginationBooks;
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
    private TableColumn<Book, Integer> stockColumn;
    //Orders
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
    @FXML
    private TextField bestCustomersTextField;
    //Add book
    @FXML
    private JFXTextField addBookTitle;
    @FXML
    private JFXTextField addBookStock;
    @FXML
    private JFXTextField addBookPrice;
    @FXML
    private JFXTextArea addBookSynopsis;
    @FXML
    private JFXDatePicker addBookPDate;
    @FXML
    private JFXComboBox<Author> addBookAuthorChoice;
    @FXML
    private JFXComboBox<Publisher> addBookPublisherChoice;
    @FXML
    private JFXComboBox<Genre> addBookGenreChoice;
    @FXML
    private JFXListView<Author> addBookAuthors;
    @FXML
    private JFXListView<Publisher> addBookPublishers;
    @FXML
    private JFXListView<Genre> addBookGenres;
    @FXML
    private JFXTextField addBookSAuthorT;
    @FXML
    private JFXTextField addBookSPublisherT;
    @FXML
    private JFXTextField addBookSGenreT;
    @FXML
    private Text wrongTitle;
    @FXML
    private Text wrongPrice;
    @FXML
    private Text wrongDate;
    @FXML
    private Text wrongStock;
    @FXML
    private Text wrongSynopsis;
    @FXML
    private Text wrongAuthor;
    @FXML
    private Text wrongPublisher;
    @FXML
    private Text wrongGenre;



    private BooksController booksController = BooksController.getInstance();
    private AuthorBookController authorBookController = AuthorBookController.getInstance();
    private AuthorsController authorsController = AuthorsController.getInstance();
    private GenresController genresController = GenresController.getInstance();
    private OrdersController ordersController = OrdersController.getInstance();
    private CustomerController customerController = CustomerController.getInstance();
    private PublishersController publishersController = PublishersController.getInstance();

    ObservableList<Book> books = FXCollections.observableArrayList();
    //ObservableMap<Integer, Author> authorsFromMap = FXCollections.observableHashMap();
    ObservableList<Order> orders = FXCollections.observableArrayList();
    private String searchingForBook = null;
    private LanguageResource lr = LanguageResource.getInstance();

    ScreenConfiguration screenConfiguration = new ScreenConfiguration();
    private final StageManager stageManager;

    @Autowired
    @Lazy
    public AdminMainController(StageManager stageManager) throws SQLException, ClassNotFoundException {
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //authorsFromMap = authorsController.getAuthors();
        createBooksTable();
        createOrderByBooksComboBox();
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

        try {
            paginationBooks.setPageCount((int) Math.ceil((double) Database.getRowsCount("books")/100));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        paginationBooks.setPageFactory(this::createBooksPage);

        AddBookInitialization();

    }

    private void createOrderByBooksComboBox() {
        orderByBooksComboBox.getItems().setAll(
                "-----",
                lr.getResources().getString("sort_by_title_asc"),       //"Book name - asc",
                lr.getResources().getString("sort_by_title_desc"),      //"Book name - desc",
                lr.getResources().getString("sort_by_author_asc"),      //"Author - asc",
                lr.getResources().getString("sort_by_author_desc"),     //"Author - desc",
                lr.getResources().getString("sort_by_price_asc"),       //"Price - asc",
                lr.getResources().getString("sort_by_price_desc"),      //"Price - desc",
                lr.getResources().getString("sort_by_date_asc"),        //"Date - asc",
                lr.getResources().getString("sort_by_date_desc")       //"Date - desc",
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
            List<Integer> authorId;
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
                //authorName.add(authorsFromMap.get(id).getName());
            }
            return new SimpleObjectProperty(String.join(", ", authorName));
        });
    }

    private Node createBooksPage(int pageNum){
        String orderBy = orderByBooksComboBox.getValue();
        try {
            if(searchingForBook != null){
                books = (ObservableList<Book>) booksController.findBook(searchingForBook, pageNum)[1];
            }
            else{
                if(orderBy == null){
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
                } else{
                    books = booksController.getBooks(pageNum, "id");
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            books = null;
        }

        bookOverviewTable.setItems(books);
        return bookOverviewTable;
    }

    public void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void handleBookSelected(){
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();

        if(book != null){
            synopsisText.setText(book.getDescription());
        }
    }

    public void handleGoToPage() throws SQLException, ClassNotFoundException {
        int pageId;
        try{
            pageId = Integer.parseInt(setPageBooksTextField.getText());
        } catch (NumberFormatException e){
            pageId = 0;
        }
        if(searchingForBook != null){
            if(pageId > 0 && pageId <= (int) Math.ceil((int) booksController.findBook(searchingForBook, 0)[0]/100.0)){
                paginationBooks.setCurrentPageIndex(pageId - 1);
            }
        } else{
            if(pageId > 0 && pageId <= (int) Math.ceil((double) Database.getRowsCount("books")/100)){
                paginationBooks.setCurrentPageIndex(pageId - 1);
            }
        }
        createBooksPage(paginationBooks.getCurrentPageIndex());
    }

    public void handleBookOrderChange() {
        createBooksPage(paginationBooks.getCurrentPageIndex());
    }

    //Searching of book by title
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

    public void handleChangeBook() throws IOException {
        //ChangeBookController changeBookController = new ChangeBookController();
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();
        if(book != null){
            screenConfiguration.setChangeBookScene(book);
            bookOverviewTable.refresh();
        }
    }

    public void handleDeleteBook() throws SQLException, ClassNotFoundException {
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();
        if(book != null) {
            boolean deleted = booksController.removeBook(book.getID());
            System.out.println("Book deleted: " + deleted);
            paginationBooks.setPageCount((int) Math.ceil((double) Database.getRowsCount("books")/100));
            createBooksPage(paginationBooks.getCurrentPageIndex());
        }
    }

    //load orders after searching of customer id
    public void handleLoadOrders() throws SQLException, ClassNotFoundException {

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
        orderCustomerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCustomer().getFirstName() + " " +
                param.getValue().getCustomer().getLastName()));
        orderCustomerIDColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(param.getValue().getCustomer().getID())));

    }

    public void createOrderDetailTable(){
        orderDetailBookColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBook().getTitle()));
        orderDetailQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailPriceColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getBook().getPrice())));
    }

    public void handleOrderSelected(){
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
                "zamietnutá"
                //"nevybavená"
        );

    }

    public void handleOrderStatusChange() throws SQLException, ClassNotFoundException {
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();
        if (order != null && !orderStatusChangeComboBox.getValue().equals(" ")){
            if(order.getStatus().equals("nevybavená")){
                ordersController.changeStatus(order, orderStatusChangeComboBox.getValue());
                orderOverviewTable.refresh();
                bookOverviewTable.refresh();
            }
        }
    }

    public void handleOrdersSetup() throws SQLException {
        createOrderTable();
        createOrderDetailTable();
        createOrderStatusComboBox();
        bestCustomersTextField.setText(customerController.getBestCustomers(3));
    }

    public void handleLogout(javafx.event.ActionEvent event) throws IOException {
//        ScreenConfiguration screenConfiguration = new ScreenConfiguration();
//        screenConfiguration.setLoginScene(event);
        stageManager.switchScene(FxmlView.LOGIN);
    }

    //Adding book
    public void AddBookInitialization() {
        //Adding book initialization of author
        addBookAuthors.setCellFactory(param -> new ListCell<Author>(){
            @Override
            protected void updateItem(Author item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        addBookAuthorChoice.valueProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                addBookAuthors.getItems().add(newValue);
            }
        });
        //Adding book initialization of publisher
        addBookPublishers.setCellFactory(param -> new ListCell<Publisher>(){
            @Override
            protected void updateItem(Publisher item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        addBookPublisherChoice.valueProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                addBookPublishers.getItems().clear();
                addBookPublishers.getItems().add(newValue);
            }
        });

        //Adding book initialization of genre
        addBookGenres.setCellFactory(param -> new ListCell<Genre>(){
            @Override
            protected void updateItem(Genre item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        addBookGenreChoice.valueProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                addBookGenres.getItems().add(newValue);
            }
        });
    }

    public void handleAddBook() throws SQLException {
        DecimalFormat df = new DecimalFormat("#.##");
        int counter = 0;
        double newPrice = 0;
        try{
            newPrice = Double.parseDouble(df.format(Double.parseDouble(addBookPrice.getText())));
            if((int)newPrice <= 9999){
                wrongPrice.setVisible(false);
                counter++;
            } else{
                wrongPrice.setVisible(true);
            }

        } catch(NumberFormatException e){
            wrongPrice.setVisible(true);
        }
        int newStock = 0;
        try{
            newStock = Integer.parseInt(addBookStock.getText());
            wrongStock.setVisible(false);
            counter++;
        } catch(NumberFormatException e){
            wrongStock.setVisible(true);
        }

        String newTitle = null;
        if(addBookTitle.getText().isEmpty() || addBookTitle.getText().trim().length() == 0){
            wrongTitle.setVisible(true);
        } else{
            newTitle = addBookTitle.getText();
            wrongTitle.setVisible(false);
            counter++;
        }

        String newSynopsis = null;
        if(addBookSynopsis.getText().isEmpty()){
            wrongSynopsis.setVisible(true);
        } else{
            newSynopsis = addBookSynopsis.getText();
            wrongSynopsis.setVisible(false);
            counter++;
        }

        LocalDate dateFromPicker = addBookPDate.valueProperty().getValue();
        Date newPublicationDate = null;
        if (dateFromPicker != null){
            newPublicationDate = Date.valueOf(dateFromPicker);
            wrongDate.setVisible(false);
            counter++;
        } else{
            wrongDate.setVisible(true);
        }

        List<Author> newAuthors = null;
        if(addBookAuthors.getItems().isEmpty()){
            wrongAuthor.setVisible(true);
        } else{
            wrongAuthor.setVisible(false);
            newAuthors = addBookAuthors.getItems();
            counter++;
        }
        Publisher newPublisher = null;
        if(addBookPublishers.getItems().isEmpty()){
            wrongPublisher.setVisible(true);
        } else{
            wrongPublisher.setVisible(false);
            newPublisher = addBookPublishers.getItems().get(0);
            counter++;
        }
        List<Genre> newGenres = null;
        if(addBookGenres.getItems().isEmpty()){
            wrongGenre.setVisible(true);
        } else{
            wrongGenre.setVisible(false);
            newGenres = addBookGenres.getItems();
            counter++;
        }

        //System.out.println(counter);
        if(counter == 8){
            Book book = new Book(newTitle, newPrice, newStock, newPublisher,
                                 newPublicationDate, newSynopsis, newGenres, newAuthors);
            booksController.addBook(book);
            addBookClear();
            paginationBooks.setPageCount((int) Math.ceil((double) Database.getRowsCount("books")/100));

            bookOverviewTable.refresh();
        }

    }

    //Clearing of fields
    public void addBookClear(){
        addBookPrice.clear();
        addBookStock.clear();
        addBookTitle.clear();
        addBookSynopsis.clear();
        addBookAuthors.getItems().clear();
        addBookPublishers.getItems().clear();
        addBookGenres.getItems().clear();
        addBookPDate.getEditor().clear();
        addBookSAuthorT.clear();
        addBookSPublisherT.clear();
        addBookSGenreT.clear();
        addBookAuthorChoice.getSelectionModel().clearSelection();
        addBookPublisherChoice.getSelectionModel().clearSelection();
        addBookGenreChoice.getSelectionModel().clearSelection();

        wrongPrice.setVisible(false);
        wrongPublisher.setVisible(false);
        wrongAuthor.setVisible(false);
        wrongSynopsis.setVisible(false);
        wrongDate.setVisible(false);
        wrongTitle.setVisible(false);
        wrongGenre.setVisible(false);
        wrongStock.setVisible(false);
    }

    //Author search in add book
    public void handleABAuthorSearch() {
        addBookAuthorChoice.getItems().clear();
        String Name = addBookSAuthorT.getText();
        ObservableList<Author> searchedAuthors = FXCollections.observableArrayList(authorsController.getAuthor(Name));
        if(searchedAuthors.isEmpty()){
            wrongAuthor.setVisible(true);
        } else{
            wrongAuthor.setVisible(false);
            addBookAuthorChoice.getItems().addAll(searchedAuthors);
            addBookAuthorChoice.setConverter(new StringConverter<Author>() {
                @Override
                public String toString(Author object) {
                    return object.getName();
                }

                @Override
                public Author fromString(String string) {
                    return null;
                }
            });
        }
    }
    //Publisher search in add book
    public void handleABPublisherSearch() {
        addBookPublisherChoice.getItems().clear();
        String Name = addBookSPublisherT.getText();
        ObservableList<Publisher> searchedPublishers = FXCollections.observableArrayList(publishersController.getPublisher(Name));
        if(searchedPublishers.isEmpty()){
            wrongPublisher.setVisible(true);
        } else{
            wrongPublisher.setVisible(false);
            addBookPublisherChoice.getItems().addAll(searchedPublishers);
            addBookPublisherChoice.setConverter(new StringConverter<Publisher>() {
                @Override
                public String toString(Publisher object) {
                    return object.getName();
                }

                @Override
                public Publisher fromString(String string) {
                    return null;
                }
            });
        }
    }
    //Genre search in add book
    public void handleABGenreSearch() {
        addBookGenreChoice.getItems().clear();
        String Name = addBookSGenreT.getText();
        ObservableList<Genre> searchedGenres = FXCollections.observableArrayList(genresController.getGenre(Name));
        if(searchedGenres.isEmpty()){
            wrongGenre.setVisible(true);
        } else{
            wrongGenre.setVisible(false);
            addBookGenreChoice.getItems().addAll(searchedGenres);
            addBookGenreChoice.setConverter(new StringConverter<Genre>() {
                @Override
                public String toString(Genre object) {
                    return object.getName();
                }

                @Override
                public Genre fromString(String string) {
                    return null;
                }
            });
        }
    }

    public void handleCancelAddBook() {
        addBookClear();
    }

    public void handleAddNewAuthor() {
        if(!addBookSAuthorT.getText().isEmpty()){
            String newAuthorName = addBookSAuthorT.getText();
            Author newAuthor = new Author(newAuthorName);
            authorsController.addAuthor(newAuthor);
            addBookAuthors.getItems().add(newAuthor);
            wrongAuthor.setVisible(false);

        }
    }

    public void handleAddNewPublisher() {
        if(!addBookSPublisherT.getText().isEmpty()){
            String newPublisherName = addBookSPublisherT.getText();
            Publisher newPublisher = new Publisher(newPublisherName);
            publishersController.addPublisher(newPublisher);
            addBookPublishers.getItems().clear();
            addBookPublishers.getItems().add(newPublisher);
            wrongPublisher.setVisible(false);
        }
    }

    public void handleAddNewGenre() {
        if(!addBookSGenreT.getText().isEmpty()){
            String newGenreName = addBookSGenreT.getText();
            Genre newGenre = new Genre(newGenreName);
            genresController.addGenre(newGenre);
            addBookGenres.getItems().add(newGenre);
            wrongGenre.setVisible(false);

        }
    }
}
