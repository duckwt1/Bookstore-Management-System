package controller;

import bus.UserBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static controller.ManagementController.check;
import static uitil.Util.*;

public class SignupController {

    @FXML
    private PasswordField confirmPasswordTF;

    @FXML
    private TextField emailTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Button signupBT;

    @FXML
    private TextField usernameTF;

    @FXML
    void signup(ActionEvent event) {
        String username = usernameTF.getText();
        String email = emailTF.getText();
        String password = passwordTF.getText();
        String confirmPassword = confirmPasswordTF.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("Please fill all fields");
        } else if (!password.equals(confirmPassword)) {
            showErrorMessage("Mật khẩu không khớp");
        } else {

            String hashedPassword = hashPassword(password);

            boolean result = new UserBus().register(username, email, hashedPassword);

            if (check) {
                ((Stage) signupBT.getScene().getWindow()).close();
            } else {
                if (result) {
                    showSuccessMessage("Sign up successfully!");
                    try {
                        Stage stage = (Stage) signupBT.getScene().getWindow();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/login.fxml"));
                        stage.getScene().setRoot(fxmlLoader.load());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showErrorMessage("Sign up failed!");
                }
            }
        }
    }

    @FXML
    void login(ActionEvent event) {
        try {
            Stage stage = (Stage) signupBT.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/login.fxml"));
            stage.getScene().setRoot(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
