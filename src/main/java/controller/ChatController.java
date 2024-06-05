package controller;

import bus.MessageBus;
import bus.UserBus;
import dao.MessageDAO;
import dto.Customer;
import dto.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Pos;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import static controller.LoginController.loggedInCustomer;

public class ChatController {

    private List<Message> messages;
    private List<Customer> customers = new UserBus().getAllCustomers();

    @FXML
    private VBox messageBox;

    @FXML
    private TextField textTF;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private Button sendBT;

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

        messageBox.prefWidthProperty().bind(messageScrollPane.widthProperty());
        messageBox.prefHeightProperty().bind(messageScrollPane.heightProperty());
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

        if (messages != null) {
            for (Message message : messages) {
                String displayText;
                Customer sender = message.getSender();
                Customer receiver = message.getReceiver();

                if (sender.getId() == loggedInCustomer.getId()) {
                    displayText = "You: " + message.getMessageContent();
                } else {
                    displayText = sender.getName() + ": " + message.getMessageContent();
                }

                if (receiver.getId() == loggedInCustomer.getId()) {
                    displayMessage(displayText, false);
                } else {
                    displayMessage(displayText, true);
                }
            }
        } else {
            System.out.println("Không có tin nhắn nào từ cơ sở dữ liệu.");
        }
    }


    public void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the user type to the server
            out.println(userType);

            // Thread to listen for messages from the server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        displayMessage(message);
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
            } else {
                new MessageBus().saveMessage(message, ADMIN_ID, loggedInCustomer.getId());
            }

            displayMessage("You: " + message, true);
        }
    }

    private void displayMessage(String message) {
        displayMessage(message, message.startsWith("You: "));
    }

    private void displayMessage(String message, boolean isSender) {
        Platform.runLater(() -> {
            Text text = new Text(message);
            text.wrappingWidthProperty().bind(messageBox.widthProperty().subtract(20));

            HBox box = new HBox();
            box.getChildren().add(text);
            box.setAlignment(isSender ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
            box.getStyleClass().add(isSender ? "message-box-sender" : "message-box-receiver");

            messageBox.getChildren().add(box);
            messageScrollPane.setVvalue(1.0);
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
}
