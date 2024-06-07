package controller;

import bus.MessageBus;
import bus.UserBus;
import dto.BookOrder;
import dto.Customer;
import dto.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import static controller.LoginController.loggedInCustomer;

public class ChatController {

    private List<Message> messages;
    private List<Customer> customers = new UserBus().getAllCustomers();
    private List<BookOrder> cart;

    private int customerId;

    @FXML
    private VBox messageBox;

    @FXML
    private TextField textTF;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private Button sendBT;

    @FXML
    private Button backBT;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String userType; // user or admin
    private final int ADMIN_ID = 4; // Assuming 4 is the ID for admin

    public void setUserType(String userType) {
        this.userType = userType;
        loadMessagesFromDatabase();
    }

    public void initialize() {
        sendBT.setOnAction(event -> sendMessage());

        // Ensure the messageBox width is bound to the width of the ScrollPane's viewport
        messageBox.prefWidthProperty().bind(messageScrollPane.widthProperty().subtract(20));
        messageBox.setPrefHeight(Control.USE_COMPUTED_SIZE);

        // Ensure ScrollPane fits the width of the content
        messageScrollPane.setFitToWidth(true);
        messageScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Disable horizontal scrollbar
    }

    private void loadMessagesFromDatabase() {
        MessageBus messageBus = new MessageBus();
        messages = null;

        Customer admin = customers.stream().filter(customer -> customer.getId() == ADMIN_ID).findFirst().orElse(null);
        Customer loggedInCust = customers.stream().filter(customer -> customer.getId() == loggedInCustomer.getId()).findFirst().orElse(null);

        if (userType.equals("User")) {
            messages = messageBus.getMessages(loggedInCust, admin);
        } else {
            messages = messageBus.getMessages(admin, loggedInCust);
        }

        List <Message> messages2 = messageBus.getMessages2();

        if (messages != null) {
            for (Message message : messages2) {
                if (userType.equals("User")) {
                    if (message.getSender().getId() == loggedInCustomer.getId()) {
                        displayMessage(message.getMessageContent(), true);
                    } else if (message.getReceiver().getId() == loggedInCustomer.getId()) {
                        displayMessage(message.getSender().getUsername() + ": " + message.getMessageContent(), false);
                    }
                }

                if (userType.equals("Admin")) {
                    if (message.getSender().getId() == ADMIN_ID) {
                        displayMessage(message.getMessageContent(), true);
                    } else if (message.getReceiver().getId() == ADMIN_ID) {
                        displayMessage(message.getSender().getUsername() + ": " + message.getMessageContent(), false);
                    }
                }
            }
        }
    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the user type to the server
            out.println(userType);

            if (userType.equals("User")) {
                out.println(loggedInCustomer.getId());
            }

            // Thread to listen for messages from the server
            new Thread(() -> {
                try {
                    // first message from the server should be the userid
                    customerId = Integer.parseInt(in.readLine());

                    String message;
                    while ((message = in.readLine()) != null) {
                        if (!message.startsWith(userType + ": ")) {
                            displayMessage(message, false);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = textTF.getText();
        if (!message.trim().isEmpty()) {
            out.println(userType + ": " + message);
            textTF.clear();

            if (userType.equals("User")) {
                new MessageBus().saveMessage(message, loggedInCustomer.getId(), ADMIN_ID);
            } else if (userType.equals("Admin")){
                new MessageBus().saveMessage(message, ADMIN_ID, customerId);
                System.out.println(loggedInCustomer.getId());
            }

            displayMessage(message, true);
        }
    }

    private void displayMessage(String message, boolean isSender) {
        Platform.runLater(() -> {
            Text text = new Text(message);
            text.wrappingWidthProperty().bind(messageBox.widthProperty().subtract(100));  // Adjust wrapping width

            TextFlow textFlow = new TextFlow(text);
            textFlow.getStyleClass().add(isSender ? "text-flow-sender" : "text-flow-receiver");
            textFlow.setPadding(new Insets(10,10,10,10));
            textFlow.setMaxWidth(250);
            textFlow.setLineSpacing(5);
            textFlow.getStyleClass().add(isSender ? "message-box-sender" : "message-box-receiver");

            HBox box = new HBox(textFlow);
            box.setAlignment(isSender ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
            box.setPadding(new Insets(5, 10, 5, 10));


            messageBox.getChildren().add(box);
            messageScrollPane.setVvalue(1.0);  // Scroll to bottom
        });
    }


    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void back(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(userType.equals("User") ? "/main/java2final/order.fxml" : "/main/java2final/dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            ((Stage) backBT.getScene().getWindow()).close();

            if (userType.equals("User")) {
                OrderBooksController orderBooksController = loader.getController();
                orderBooksController.setCartList(cart);
            } else if (userType.equals("Admin")) {

            }

            Stage stage = new Stage();
            stage.setTitle("Order Books");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        closeConnection();
    }

    public void setCartList(List<BookOrder> cart) {
        this.cart = cart;
    }
}
