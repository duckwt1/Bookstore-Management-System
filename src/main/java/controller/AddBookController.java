package controller;

import bus.BookBus;
import dto.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static uitil.Util.showErrorMessage;

public class AddBookController {

    @FXML
    private Button addBookBT;

    @FXML
    private TextField authorTF;

    @FXML
    private TextField priceTF;

    @FXML
    private TextField titleTF;

    @FXML
    void addBook(ActionEvent event) {
        String title = titleTF.getText();
        String author = authorTF.getText();
        String price = priceTF.getText();

        if (title.isEmpty() || author.isEmpty() || price.isEmpty() || !price.matches("\\d+(\\.\\d+)?")) {
            showErrorMessage("Please fill all fields!");
        } else {
            new BookBus().addBook(title, author, Double.parseDouble(price));
            ((Stage) addBookBT.getScene().getWindow()).close();
        }
    }

}
