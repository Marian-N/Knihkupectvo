package gui.usermain.confirmation;

import com.jfoenix.controls.JFXButton;
import controller.OrdersController;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Book;
import model.Order;

import java.sql.SQLException;


public class CancelConfirmation {

    @FXML
    private JFXButton yesButton;
    @FXML
    private JFXButton noButton;

    private OrdersController ordersController = OrdersController.getInstance();

    public CancelConfirmation() throws SQLException, ClassNotFoundException {
    }

    public void cancelConfirm(Order order, Stage stage){
        yesButton.setOnAction(e->{
            try {
                ordersController.changeStatus(order, "zrušená");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            stage.close();
        });
        noButton.setOnAction(e->stage.close());

    }
}
