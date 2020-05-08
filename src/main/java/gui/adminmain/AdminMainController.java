package gui.adminmain;

import com.jfoenix.controls.*;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.*;

import gui.adminchangebook.ChangeBookController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
    private BookGenreController bookGenreController = BookGenreController.getInstance();
    private GenresController genresController = GenresController.getInstance();
    private OrdersController ordersController = OrdersController.getInstance();
    private CustomerController customerController = CustomerController.getInstance();
    private PublishersController publishersController = PublishersController.getInstance();

    ObservableList<Book> books = FXCollections.observableArrayList();
    ObservableMap<Integer, Author> authorsFromMap = FXCollections.observableHashMap();
   // ObservableMap<Integer, Genre> genresFromMap = FXCollections.observableHashMap();
    ObservableList<Order> orders = FXCollections.observableArrayList();

    ScreenConfiguration screenConfiguration = new ScreenConfiguration();


    public AdminMainController() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        try {
//            genresFromMap = genresController.getGenres();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        authorsFromMap = authorsController.getAuthors();
        createBooksTable();
        createOrderByBooksComboBox();
        //findBook();
        try {
            paginationBooks.setPageCount((int) Math.ceil((double) Database.getRowsCount("books")/100));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        paginationBooks.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createBooksPage(pageIndex);
            }
        });

        AddBookInitialization();

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
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        genreColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, List<String>>, ObservableValue<List<String>>>() {
            @Override
            public ObservableValue<List<String>> call(TableColumn.CellDataFeatures<Book, List<String>> param) {
                return new SimpleObjectProperty(param.getValue().getGenres().getStringGenres());
            }
        });

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

                List<String> authorName = new ArrayList<>();
                //taking only names from hashmap
                for (Integer id : authorId){
                    authorName.add(authorsFromMap.get(id).getName());
                }
                return new SimpleObjectProperty(String.join(", ", authorName));
            }
        });
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

    public void handleGoToPage(ActionEvent actionEvent) throws SQLException {
        int pageId = 0;
        try{
            pageId =Integer.parseInt(setPageBooksTextField.getText());
        } catch (NumberFormatException e){
            pageId = 0;
        }

        if(pageId > 0 && pageId <= (int) Math.ceil((double) Database.getRowsCount("books")/100)){
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
            bookOverviewTable.refresh();
        }
    }

    public void handleDeleteBook(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Book book = bookOverviewTable.getSelectionModel().getSelectedItem();
        if(book != null) {
            boolean deleted = booksController.removeBook(book.getID());
            System.out.println("Book deleted: " + deleted);
            bookOverviewTable.refresh();
        }
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
                "zamietnutá"
                //"nevybavená"
        );

    }

    public void handleOrderStatusChange(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Order order = orderOverviewTable.getSelectionModel().getSelectedItem();
        if (order != null && orderStatusChangeComboBox.getValue() != " "){
            if(order.getStatus().equals("nevybavená")){
                ordersController.changeStatus(order, orderStatusChangeComboBox.getValue());
                orderOverviewTable.refresh();
            }

        }
    }

    public void handleOrdersSetup(Event event) throws SQLException {
        createOrderTable();
        createOrderDetailTable();
        createOrderStatusComboBox();
        bestCustomersTextField.setText(customerController.getBestCustomers(3));
    }

    public void handleLogout(javafx.event.ActionEvent event) throws IOException {
        ScreenConfiguration screenConfiguration = new ScreenConfiguration();
        screenConfiguration.setLoginScene(event);
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

    public void handleAddBook(ActionEvent event) {
        DecimalFormat df = new DecimalFormat("#.##");
        int counter = 0;

        double newPrice = 0;
        try{
            newPrice = Double.parseDouble(df.format(Double.parseDouble(addBookPrice.getText())));
            wrongPrice.setVisible(false);
            counter++;
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
        if(addBookTitle.getText().isEmpty()){
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

        System.out.println(counter);
        if(counter == 8){
            Book book = new Book(newTitle, newPrice, newStock, newPublisher,
                                 newPublicationDate, newSynopsis, newGenres, newAuthors);
            booksController.addBook(book);
            addBookClear();

        }
        bookOverviewTable.refresh();

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
    public void handleABAuthorSearch(ActionEvent event) {
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
    public void handleABPublisherSearch(ActionEvent event) {
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
    public void handleABGenreSearch(ActionEvent event) {
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

    public void handleCancelAddBook(ActionEvent event) {
        addBookClear();
    }

    public void handleAddNewAuthor(ActionEvent event) {
        if(!addBookSAuthorT.getText().isEmpty()){
            String newAuthorName = addBookSAuthorT.getText();
            Author newAuthor = new Author(newAuthorName);
            authorsController.addAuthor(newAuthor);
            addBookAuthors.getItems().add(newAuthor);
            wrongAuthor.setVisible(false);

        }
    }

    public void handleAddNewPublisher(ActionEvent event) {
        if(!addBookSPublisherT.getText().isEmpty()){
            String newPublisherName = addBookSPublisherT.getText();
            Publisher newPublisher = new Publisher(newPublisherName);
            publishersController.addPublisher(newPublisher);
            addBookPublishers.getItems().clear();
            addBookPublishers.getItems().add(newPublisher);
            wrongPublisher.setVisible(false);
        }
    }

    public void handleAddNewGenre(ActionEvent event) {
        if(!addBookSGenreT.getText().isEmpty()){
            String newGenreName = addBookSGenreT.getText();
            Genre newGenre = new Genre(newGenreName);
            genresController.addGenre(newGenre);
            addBookGenres.getItems().add(newGenre);
            wrongGenre.setVisible(false);

        }
    }
}
