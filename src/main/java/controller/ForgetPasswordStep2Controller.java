package controller;

import bus.UserBus;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import static uitil.Util.*;

public class ForgetPasswordStep2Controller {

    private int OTP;
    private String email;

    @FXML
    private TextField otpTF;

    @FXML
    private PasswordField newPasswordTF;

    @FXML
    private PasswordField confirmNewPasswordTF;

    @FXML
    void initialize() {
        newPasswordTF.setDisable(true);
        confirmNewPasswordTF.setDisable(true);
    }

    @FXML
    private void verifyOTP() {
        String otp = otpTF.getText();

        if (otp.isEmpty()) {
            showErrorMessage("OTP field must be filled out.");
            return;
        }

        boolean otpValid = verifyOTP(otp);

        if (!otpValid) {
            showErrorMessage("Invalid OTP.");
        } else {
            showSuccessMessage("OTP verified successfully.");
            newPasswordTF.setDisable(false);
            confirmNewPasswordTF.setDisable(false);
        }
    }

    @FXML
    private void changePassword() {
        String newPassword = newPasswordTF.getText();
        String confirmNewPassword = confirmNewPasswordTF.getText();

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showErrorMessage("All fields must be filled out.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            showErrorMessage("Passwords do not match.");
            return;
        }

        UserBus userBus = new UserBus();
        boolean success = userBus.resetPassword(email, hashPassword(newPassword));

        if (success) {
            showSuccessMessage("Password reset successfully.");
        } else {
            showErrorMessage("Error resetting password.");
        }
    }

    private boolean verifyOTP(String otp) {
        if (otp.equals(String.valueOf(this.OTP))) {
            return true;
        }
        return false;
    }

    public void setOTPandEmail(int otp, String email) {
        this.OTP = otp;
        this.email = email;
    }
}
