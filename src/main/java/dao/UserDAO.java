package dao;

import dto.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDAO {
    private SessionFactory sessionFactory;

    public UserDAO(){
        sessionFactory = Connection.getSessionFactory();
    }

    public boolean login(String username, String password){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        boolean result = session.createQuery("from Customer where username = :username and password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult() != null;
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Customer> getAllCustomers(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Customer> customers = session.createQuery("from Customer").list();
        session.getTransaction().commit();
        session.close();
        return customers;
    }

    public boolean register(String username, String email, String password){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String hql = "insert into Customer(username, email, password) values(:username, :email, :password)";
        session.createQuery(hql)
                .setParameter("username", username)
                .setParameter("email", email)
                .setParameter("password", password)
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
        return true;
    }

    public void deleteCustomer(int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("delete from Customer where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void updateCustomer(Customer customer){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(customer);
        session.getTransaction().commit();
        session.close();
    }

    public boolean resetPassword(String email, String newPassword){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Customer customer = (Customer) session.createQuery("from Customer where email = :email")
                .setParameter("email", email)
                .uniqueResult();
        if (customer == null) {
            session.getTransaction().commit();
            session.close();
            return false;
        }
        customer.setPassword(newPassword);
        session.update(customer);
        session.getTransaction().commit();
        session.close();
        return true;
    }
}
