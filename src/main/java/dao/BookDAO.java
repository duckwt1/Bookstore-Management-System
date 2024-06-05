package dao;

import dto.Book;
import org.hibernate.Session;

import java.util.List;

public class BookDAO {
    private Session session;

    public BookDAO() {
        this.session = Connection.getSessionFactory().openSession();
    }

    public List<Book> getAllBooks() {
        session.beginTransaction();

        List<Book> books = session.createQuery("from Book", Book.class).list();

        session.getTransaction().commit();

        return books;
    }

    public void addBook(String title, String author, double price) {
        session.beginTransaction();

        String hql = "insert into Book(title, author, price) values(:title, :author, :price)";
        session.createQuery(hql)
                .setParameter("title", title)
                .setParameter("author", author)
                .setParameter("price", price)
                .executeUpdate();

        session.getTransaction().commit();
    }

    public void updateBook(Book book) {
        session.beginTransaction();

        session.update(book);

        session.getTransaction().commit();
    }
}
