package controller;

import bus.UserBus;
import dto.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static uitil.Util.hashPassword;
import static uitil.Util.showErrorMessage;

public class LoginController {

    public static Customer loggedInCustomer = null;

    private List<Customer> customers = new UserBus().getAllCustomers();

    @FXML
    private CheckBox checkbox;

    @FXML
    private Button forgotBT;

    @FXML
    private Button loginnBT;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TextField usernameTF;

    @FXML
    void initialize() {
        checkbox.setOnAction(event -> {

            if (checkbox.isSelected()) {
                passwordTF.setPromptText(passwordTF.getText());
                passwordTF.setText("");
                passwordTF.setDisable(true);
            } else {
                passwordTF.setText(passwordTF.getPromptText());
                passwordTF.setPromptText("Password");
                passwordTF.setDisable(false);
            }
        });
    }

    @FXML
    void forgotpassword(ActionEvent event) {
        try {
            Stage stage = (Stage) loginnBT.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/forgetpass.fxml"));
            stage.getScene().setRoot(fxmlLoader.load());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void login(ActionEvent event) {

        loggedInCustomer = null;

        String username = usernameTF.getText();
        String password = passwordTF.getText();

        for (Customer customer : customers) {
            if (username.equalsIgnoreCase(customer.getUsername()) && hashPassword(password).equals(customer.getPassword())) {
                loggedInCustomer = customer;
                System.out.println(customer.getUsername() + " " + customer.getPassword());
                break;
            }
        }
        if (loggedInCustomer != null) {
            if ("admin".equalsIgnoreCase(username) && "admin".equalsIgnoreCase(password)) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/dashboard.fxml"));
                    stage.setTitle("Dashboard");
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.show();
                    ((Stage) loginnBT.getScene().getWindow()).close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/order.fxml"));
                    stage.setTitle("Home");
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.show();
                    ((Stage) loginnBT.getScene().getWindow()).close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            showErrorMessage("Invalid username or password");
        }

    }

    @FXML
    void signup(ActionEvent event) {
        try {
            Stage stage = (Stage) loginnBT.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/signup.fxml"));
            stage.getScene().setRoot(fxmlLoader.load());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
