package gui.adminchangebook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.BooksController;
import javafx.fxml.FXML;
import model.Book;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Class used by admin
 * Handles changes of stock quantity and price of books
 */
public class ChangeBookController {
    @FXML
    private JFXTextField changePriceTextField;
    @FXML
    private JFXTextField changeStockTextField;
    @FXML
    private TextField bookTitle;
    @FXML
    private TextField bookId;
    @FXML
    private TextField bookPrice;
    @FXML
    private TextField bookStock;
    @FXML
    private JFXButton changePriceButton;
    @FXML
    private JFXButton changeStockButton;

    private BooksController booksController = BooksController.getInstance();

    public ChangeBookController() throws SQLException, ClassNotFoundException {
    }


    public void initData(Book book){
        bookTitle.setText(String.valueOf(book.getTitle()));
        bookId.setText(String.valueOf(book.getID()));
        bookPrice.setText(String.valueOf(book.getPrice()));
        bookStock.setText(String.valueOf(book.getStockQuantity()));
        changePriceButton.setOnAction(e->handleChangePrice(book));
        changeStockButton.setOnAction(e->handleChangeStock(book));

    }

    public void handleChangePrice(Book book) {
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            double newPrice = Double.parseDouble(df.format(Double.parseDouble(changePriceTextField.getText())));
            if(newPrice < 10000){   //price can be changed only if its less than 4 digits
                booksController.changeBook(book.getID(), newPrice);
                book.setPrice(newPrice);
            } else{
                changePriceTextField.clear();
            }
        } catch(NumberFormatException ex) {
            changePriceTextField.clear();
        }
    }

    public void handleChangeStock(Book book) {
        try{
            int newStock = Integer.parseInt(changeStockTextField.getText());
            booksController.changeBook(book.getID(), newStock);
            book.setStockQuantity(newStock);
        } catch(NumberFormatException ex) {
            changeStockTextField.clear();
        }
    }
}
