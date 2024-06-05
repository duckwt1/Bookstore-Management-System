package controller;

import bus.UserBus;
import dto.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static uitil.Util.sendEmail;
import static uitil.Util.showErrorMessage;

public class ForgetPasswordStep1Controller {

    private List<Customer> customers = new UserBus().getAllCustomers();

    @FXML
    private TextField emailTF;

    @FXML
    private void checkEmail() {
        String email = emailTF.getText();

        if (email.isEmpty()) {
            showErrorMessage("Email field must be filled out.");
            return;
        }


        boolean emailExists = false;

        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                emailExists = true;
                break;
            }
        }

        if (emailExists) {
            loadOtpAndResetPasswordScene(email);
        } else {
            showErrorMessage("Email not found.");
        }
    }

    private void loadOtpAndResetPasswordScene(String email) {

        int OTP;
        Random random = new Random();
        OTP = random.nextInt(999999);
        System.out.println(OTP);

        sendEmail(email, "OTP for resetting password", "Your OTP is: " + OTP);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java2final/forgetpassstep2.fxml"));
            Stage stage = (Stage) emailTF.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            ForgetPasswordStep2Controller controller = loader.getController();
            controller.setOTPandEmail(OTP, email);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
