package gui.usermain.makeorder;

import com.jfoenix.controls.JFXButton;
import controller.CustomerController;
import gui.usermain.UserMainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Book;
import model.Customer;
import model.Order;
import model.OrderContent;

import java.sql.SQLException;

public class MakeOrderController {

    @FXML
    private TableView<OrderContent> orderMakeTable;
    @FXML
    private TableColumn<OrderContent, String> booksInOrderColumn;
    @FXML
    private TableColumn<OrderContent, Integer> quantityOfBooksColumn;
    @FXML
    private TableColumn<OrderContent, String> priceOfBookColumn;
    @FXML
    private JFXButton completeOrderButton;
    @FXML
    private JFXButton cancelOrderButton;
    @FXML
    private JFXButton removeItemButton;
    @FXML
    private TextField totalCostTextField;
    @FXML
    private JFXButton changeQuantityButton;
    @FXML
    private TextField changeQuantityTextField;


    ObservableList<OrderContent> newOrder;
    private CustomerController customerController = CustomerController.getInstance();

    public MakeOrderController() throws SQLException, ClassNotFoundException {
    }

    public void initData(ObservableList<OrderContent> newOrderTaken, int customerId, Stage stage){
        createTable();
        newOrder = newOrderTaken;
        if(newOrder != null){
            fillTable(newOrder);
            double totalPrice = getTotalPrice(newOrder);
            totalCostTextField.setText(String.valueOf(totalPrice));

            removeItemButton.setOnAction(e->{
                OrderContent selectedItem = orderMakeTable.getSelectionModel().getSelectedItem();
                if(newOrder.contains(selectedItem)){
                    newOrder.remove(selectedItem);
                }
            });

            changeQuantityButton.setOnAction(e->{
                OrderContent selectedItem = orderMakeTable.getSelectionModel().getSelectedItem();
                if(newOrder.contains(selectedItem)){
                    int idOfOrder = newOrder.indexOf(orderMakeTable.getSelectionModel().getSelectedItem());
                    try{
                        int newQuantity = Integer.parseInt(changeQuantityTextField.getText());
                        if(newOrder.get(idOfOrder).getBook().getStockQuantity() >= newQuantity && newQuantity > 0){
                            newOrderTaken.get(idOfOrder).setQuantity(newQuantity);
                            orderMakeTable.refresh();
                            totalCostTextField.setText(String.valueOf(getTotalPrice(newOrder)));
                            changeQuantityTextField.clear();
                        }
                        else{
                            changeQuantityTextField.clear();
                        }
                    }catch(NumberFormatException ex){
                        changeQuantityTextField.clear();
                    }
                }
            });

            cancelOrderButton.setOnAction(e->{
                newOrder.clear();
                stage.close();
            });

            completeOrderButton.setOnAction(e->{
                ObservableList<OrderContent> newnewOrder = null;
                Order order = null;
                try {
                    newnewOrder = newOrder;
                    order = new Order(customerController.getCustomer(customerId), newnewOrder);
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                newOrder.clear();
                stage.close();
            });
        }

    }

    private double getTotalPrice(ObservableList<OrderContent> newOrder) {
        double total = 0;
        for(OrderContent orderContent:newOrder)
            total += orderContent.getBook().getPrice() * orderContent.getQuantity();
        return total;
    }

    public ObservableList<OrderContent> getNewOrder(){
        return newOrder;
    }

    private void fillTable(ObservableList<OrderContent> newOrder) {
        orderMakeTable.setItems(newOrder);
    }

    public void createTable(){
        booksInOrderColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderContent, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderContent, String> param) {
                return new SimpleStringProperty(param.getValue().getBook().getTitle());
            }
        });

        priceOfBookColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderContent, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderContent, String> param) {
                return new SimpleStringProperty(String.valueOf(param.getValue().getBook().getPrice()));
            }
        });

        quantityOfBooksColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

}
