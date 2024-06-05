package dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Connection {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(dto.Book.class);
            configuration.addAnnotatedClass(dto.Customer.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
