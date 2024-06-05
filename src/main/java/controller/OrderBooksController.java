package controller;

import bus.BookBus;
import dto.Book;
import dto.BookOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static controller.LoginController.loggedInCustomer;
import static uitil.Util.showErrorMessage;
import static uitil.Util.showSuccessMessage;

public class OrderBooksController {

    private List<Book> books = new BookBus().getAllBooks();

    private List<Book> filteredBooks;

    private FlowPane bookFlowPane;

    private List<BookOrder> cart;

    @FXML
    private ScrollPane orderScrollpane;

    @FXML
    private Button cartBT;

    @FXML
    private TextField searchTF;

    @FXML
    private Button chatBT;

    @FXML
    void initialize() {
        if (cart == null) {
            cart = new ArrayList<>();
        }

        filteredBooks = new ArrayList<>(books);
        loadBooks();

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));
    }

    @FXML
    void viewCart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java2final/cart.fxml"));
            Scene scene = new Scene(loader.load());
            CartController controller = loader.getController();
            controller.setCartList(cart);
            ((Stage) cartBT.getScene().getWindow()).setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void chat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java2final/chat.fxml"));
            Scene scene = new Scene(loader.load());
            ((Stage) chatBT.getScene().getWindow()).close();
            Stage stage = new Stage();
            stage.setTitle("Chat - " + loggedInCustomer.getUsername());
            stage.setScene(scene);
            stage.show();

            ChatController chatController = loader.getController();
            chatController.setUserType("User");
            chatController.connectToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() {
        bookFlowPane = new FlowPane();
        bookFlowPane.setHgap(40);
        bookFlowPane.setVgap(20);
        bookFlowPane.setPadding(new Insets(15, 10, 15, 10));

        for (Book book : filteredBooks) {
            VBox bookBox = createBookBox(book);
            bookFlowPane.getChildren().add(bookBox);
        }

        bookFlowPane.minWidthProperty().bind(orderScrollpane.widthProperty());
        bookFlowPane.minHeightProperty().bind(orderScrollpane.heightProperty());
        orderScrollpane.setContent(bookFlowPane);
    }

    private VBox createBookBox(Book book) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        vbox.setPrefWidth(300);

        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        title.setWrapText(true);
        title.setMaxWidth(280);

        Label author = new Label(book.getAuthor());
        author.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        author.setWrapText(true);
        author.setMaxWidth(280);

        Label price = new Label("Price: $" + book.getPrice());
        price.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        price.setWrapText(true);
        price.setMaxWidth(280);

        HBox quantityBox = new HBox();
        quantityBox.setSpacing(10);

        Button decreaseButton = new Button("-");
        Label quantityLabel = new Label("1");
        Button increaseButton = new Button("+");

        decreaseButton.setOnAction(event -> {
            int currentQuantity = Integer.parseInt(quantityLabel.getText());
            if (currentQuantity > 1) {
                quantityLabel.setText(String.valueOf(currentQuantity - 1));
            }
        });

        increaseButton.setOnAction(event -> {
            int currentQuantity = Integer.parseInt(quantityLabel.getText());
            quantityLabel.setText(String.valueOf(currentQuantity + 1));
        });

        quantityBox.getChildren().addAll(decreaseButton, quantityLabel, increaseButton);

        Button addButton = new Button("Add to Cart");
        addButton.setOnAction(event -> handleAddToCart(book, Integer.parseInt(quantityLabel.getText())));

        vbox.getChildren().addAll(title, author, price, quantityBox, addButton);

        return vbox;
    }

    private void filterBooks(String query) {
        filteredBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        loadBooks();
    }

    public void handleAddToCart(Book book, int quantity) {
        try {
            BookOrder bookOrder = new BookOrder(book, quantity);
            cart.add(bookOrder);
            showSuccessMessage("Book added to cart successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Failed to add book to cart.");
        }
    }

    public void setCartList(List<BookOrder> cart) {
        this.cart = cart;

        for (BookOrder bookOrder : cart) {
            System.out.println(bookOrder.getBook().getTitle() + " - " + bookOrder.getQuantity());
        }
    }
}
