package controller;

import bus.BookBus;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.math.BigDecimal;

import static controller.ManagementController.selectedBook;
import static uitil.Util.showErrorMessage;
import static uitil.Util.showSuccessMessage;

public class EditBookController {

    @FXML
    private TextField titleTF;

    @FXML
    private TextField authorTF;

    @FXML
    private TextField priceTF;

    @FXML
    private Button editBookBT;


    public void initialize() {
        if (selectedBook != null) {
            titleTF.setText(selectedBook.getTitle());
            authorTF.setText(selectedBook.getAuthor());
            priceTF.setText(String.valueOf(selectedBook.getPrice()));
        }
    }

    @FXML
    private void editBook() {
        String title = titleTF.getText();
        String author = authorTF.getText();
        String priceText = priceTF.getText();
        BigDecimal price;

        try {
            price = new BigDecimal(priceText);
        } catch (NumberFormatException e) {
            showErrorMessage("Price must be a number.");
            return;
        }

        if (title.isEmpty() || author.isEmpty() || price.compareTo(BigDecimal.ZERO) <= 0) {
            showErrorMessage("All fields must be filled out correctly.");
            return;
        }

        selectedBook.setTitle(title);
        selectedBook.setAuthor(author);
        selectedBook.setPrice(price);

        BookBus bookBus = new BookBus();
        bookBus.updateBook(selectedBook);

        showSuccessMessage("Book updated successfully.");
    }
}
