package controller;

import bus.UserBus;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static controller.ManagementController.selectedCustomer;
import static uitil.Util.hashPassword;

public class EditCustomerController {

    @FXML
    private TextField usernameTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private TextField confirmPasswordTF;

    @FXML
    private Button editCustomerBT;



    public void initialize() {
        // Load selected customer details into the fields
        if (selectedCustomer != null) {
            usernameTF.setText(selectedCustomer.getName());
            emailTF.setText(selectedCustomer.getEmail());
            passwordTF.setText(selectedCustomer.getPassword());
            confirmPasswordTF.setText(selectedCustomer.getPassword());
        }
    }

    @FXML
    private void editCustomer() {
        String username = usernameTF.getText();
        String email = emailTF.getText();
        String password = passwordTF.getText();
        String confirmPassword = confirmPasswordTF.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
            showAlert("Invalid Input", "All fields must be filled out correctly and passwords must match.");
            return;
        }

        selectedCustomer.setName(username);
        selectedCustomer.setEmail(email);
        selectedCustomer.setPassword(hashPassword(password));

        UserBus userBus = new UserBus();
        userBus.updateCustomer(selectedCustomer);

        showAlert("Success", "Customer updated successfully.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
