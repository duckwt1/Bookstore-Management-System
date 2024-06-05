package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardController {

    public static String selectedTable = "customer";

    @FXML
    private Button bookBT;

    @FXML
    private Button logoutBT;

    @FXML
    private BorderPane mainBorderpane;

    @FXML
    private Button orderBT;

    @FXML
    private Button staticBT;

    @FXML
    private Button userBT;

    @FXML
    private Button chatBT;

    @FXML
    void initialize() {
        loadManagementView();
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

            ((Stage) logoutBT.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void bookManagement(ActionEvent event) {
        selectedTable = "book";
        loadManagementView();
    }

    @FXML
    void ordersManagement(ActionEvent event) {
        selectedTable = "order";
        loadManagementView();
    }

    @FXML
    void staticManagement(ActionEvent event) {
        selectedTable = "static";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/static.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            mainBorderpane.setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void useerManagement(ActionEvent event) {
        selectedTable = "customer";
        loadManagementView();
    }

    private void loadManagementView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/managemant.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            mainBorderpane.setCenter(pane);
        } catch (IOException e) {
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
            stage.setTitle("Chat - Admin");
            stage.setScene(scene);
            stage.show();

            ChatController chatController = loader.getController();
            chatController.setUserType("Admin");
            chatController.connectToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
