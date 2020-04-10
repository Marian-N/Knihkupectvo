package controller;

import javafx.collections.ObservableList;
import model.Order;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class OrdersControllerTest {

    @Test
    public void getOrders() throws SQLException, ClassNotFoundException {
        OrdersController oc = OrdersController.getInstance();
        int customerID = 5;
        ObservableList<Order> orders = oc.getOrders(customerID);
        for(Order order:orders) {
            assertEquals(order.getCustomer().getID(), customerID);
        }
    }
}