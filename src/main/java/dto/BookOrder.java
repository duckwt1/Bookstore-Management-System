package dto;

public class BookOrder {
    private Book book;
    private int quantity;

    public BookOrder(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }
}