package controller;

import bus.OrderBus;
import dto.Book;
import dto.BookOrder;
import dto.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static controller.LoginController.loggedInCustomer;
import static uitil.Util.sendEmail;

public class CartController {

    private List<BookOrder> cart = new ArrayList<>();

    private VBox cartBox;

    private double total = 0;

    @FXML
    private Button backBT;

    @FXML
    private ScrollPane cartScrollpane;

    @FXML
    private Button totalLB;

    @FXML
    private Button buyBT;

    @FXML
    void initialize() {
        // Initialize any necessary components
    }

    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java2final/order.fxml"));
            Scene scene = new Scene(loader.load());
            ((Stage) backBT.getScene().getWindow()).setScene(scene);
            OrderBooksController controller = loader.getController();
            controller.setCartList(cart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCart() {
        cartBox = new VBox();
        cartBox.setSpacing(10);
        cartBox.setPadding(new javafx.geometry.Insets(10));
        cartBox.getChildren().clear();

        total = 0; // Reset total to 0 before recalculating

        for (BookOrder bookOrder : cart) {
            VBox bookCard = new VBox();
            bookCard.setSpacing(5);
            bookCard.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

            Label titleLabel = new Label("Title: " + bookOrder.getBook().getTitle());
            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Label quantityLabel = new Label("Quantity: " + bookOrder.getQuantity());
            quantityLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Label priceLabel = new Label("Price: " + bookOrder.getBook().getPrice());
            priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            total += ((bookOrder.getBook().getPrice()).doubleValue() * bookOrder.getQuantity());

            Button deleteBT = new Button("Remove");
            deleteBT.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px;");
            deleteBT.setOnAction(actionEvent -> deleteBook(bookOrder));

            bookCard.getChildren().addAll(titleLabel, quantityLabel, priceLabel, deleteBT);
            cartBox.getChildren().add(bookCard);
        }

        cartBox.minWidthProperty().bind(cartScrollpane.widthProperty());
        cartBox.minHeightProperty().bind(cartScrollpane.heightProperty());

        cartScrollpane.setContent(cartBox);

        totalLB.setText("Total: $" + total);
    }

    private void deleteBook(BookOrder bookOrder) {
        cart.remove(bookOrder);
        loadCart();
    }

    @FXML
    void order() {
        List<Order> orders = new OrderBus().getAllOrders();

        new OrderBus().addOrder(loggedInCustomer.getId());

        int orderID = new OrderBus().getAllOrders().getLast().getId();
        String orderDate = new OrderBus().getAllOrders().getLast().getOrderDate().toString();

        for (BookOrder bookOrder : cart) {
            new OrderBus().addOrderDetail(orderID, bookOrder.getBook().getId(), bookOrder.getQuantity());
        }

        String s = "";

        for (BookOrder bookOrder : cart) {
            s += "BookID: " + bookOrder.getBook().getId() + " - Title: " + bookOrder.getBook().getTitle() + " - Quantity: " + bookOrder.getQuantity() + " - Price: " + bookOrder.getBook().getPrice() + "\n";
        }

        s += "Total: " + total;

        String string = "\n" +
                "Dear " + loggedInCustomer.getName() + ",\n" +
                "Thank you for ordering from our store.\n" + "\n" + "Your OrderID " + orderID + " on " + orderDate + " has been verified.\n" +
                s + "\n" + "\nWe hope you enjoyed your shopping experience with us and that you will visit us again soon.\n";

        sendEmail(loggedInCustomer.getEmail(), "Invoice", string);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java2final/invoice.fxml"));
            Scene scene = new Scene(loader.load());
            ((Stage) buyBT.getScene().getWindow()).setScene(scene);

            InvoiceController controller = loader.getController();
            controller.setInvoiceList(cart, total);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCartList(List<BookOrder> cart) {
        this.cart = cart;
        loadCart();
    }
}
