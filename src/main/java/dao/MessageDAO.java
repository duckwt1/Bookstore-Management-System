package dao;

import dto.Customer;
import dto.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class MessageDAO {
    private SessionFactory sessionFactory;

    public MessageDAO() {
        sessionFactory = Connection.getSessionFactory();
    }

    public void saveMessage(String message, int sender, int receiver) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "INSERT INTO Message (messageContent, sender, receiver, createdAt) VALUES (:message, :sender, :receiver, current timestamp )";
            session.createQuery(hql)
                    .setParameter("message", message)
                    .setParameter("sender", sender)
                    .setParameter("receiver", receiver)
                    .executeUpdate();
            session.getTransaction().commit();
            System.out.println(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessages(Customer sender, Customer receiver) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Message WHERE sender = :sender AND receiver = :receiver";
            return session.createQuery(hql, Message.class)
                    .setParameter("sender", sender)
                    .setParameter("receiver", receiver)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getMessages2(){
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Message";
            return session.createQuery(hql, Message.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
