package bus;

import dao.BookDAO;
import dto.Book;

import java.util.List;

public class BookBus {
    public List<Book> getAllBooks() {
        return new BookDAO().getAllBooks();
    }

    public void addBook(String title, String author, double price) {
        new BookDAO().addBook(title, author, price);
    }

    public void updateBook(Book book) {
        new BookDAO().updateBook(book);
    }
}
