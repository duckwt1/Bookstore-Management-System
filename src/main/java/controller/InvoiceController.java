package controller;

import dto.BookOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class InvoiceController {

    private List<BookOrder> invoice;
    private double total;

    private VBox invoiceBox;

    @FXML
    private ScrollPane invoiceScrollpane;

    @FXML
    private Button logoutBT;

    @FXML
    private Button order;

    @FXML
    void initialize() {

    }

    @FXML
    void logout(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java2final/login.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.show();
            ((Stage) logoutBT.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void orderAgain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java2final/order.fxml"));
            Scene scene = new Scene(loader.load());
            ((Stage) order.getScene().getWindow()).setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadInvoice() {
        invoiceBox = new VBox();
        invoiceBox.setSpacing(10);
        invoiceBox.setPadding(new javafx.geometry.Insets(10));

        for (BookOrder bookOrder : invoice){
            String information = "ID: " + bookOrder.getBook().getId() + " - Book: " + bookOrder.getBook().getTitle() + " - " +
                    "Quantity: " + bookOrder.getQuantity() + " - " +
                    "Price: " + bookOrder.getBook().getPrice() + " - " +
                    "Total: " + bookOrder.getQuantity() * (bookOrder.getBook().getPrice()).doubleValue() + "\n";

            Label invoiceLabel = new Label(information);
            invoiceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            invoiceBox.getChildren().add(invoiceLabel);
        }

        Label lb = new Label("-------------------------------------------------");
        lb.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        invoiceBox.getChildren().add(lb);

        Label totalLabel = new Label("Total: " + total);
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        invoiceBox.getChildren().add(totalLabel);

        invoiceBox.minWidthProperty().bind(invoiceScrollpane.widthProperty());
        invoiceBox.minHeightProperty().bind(invoiceScrollpane.heightProperty());

        invoiceScrollpane.setContent(invoiceBox);
    }

    public void setInvoiceList(List<BookOrder> invoice, double total) {
        this.invoice = invoice;
        this.total = total;

        loadInvoice();
    }
}
