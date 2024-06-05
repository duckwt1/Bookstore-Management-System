package main.java2final;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bookstore Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Platform.runLater(() -> {
            try {
                new Application().start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}