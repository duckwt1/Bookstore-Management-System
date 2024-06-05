package dao;

import dto.Order;
import dto.OrderDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class OrderDAO {
    private SessionFactory sessionFactory;

    public OrderDAO(){
        sessionFactory = Connection.getSessionFactory();
    }

    public List<Order> getAllOrders(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Order> orders = session.createQuery("from Order").list();
        session.getTransaction().commit();
        session.close();
        return orders;
    }

    public List<OrderDetail> getAllOrderDetails() {
        List<OrderDetail> orderDetails = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            orderDetails = session.createQuery("from OrderDetail od join fetch od.orderID join fetch od.bookID", OrderDetail.class).list();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return orderDetails;
    }

    public int addOrder(int cusID){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int id = -1;

        String hqlInsert = "insert into Order (customerID, orderDate) values (:cusID, CURRENT_TIMESTAMP )";
        session.createQuery(hqlInsert)
                .setParameter("cusID", cusID)
                .executeUpdate();

        String hqlSelect = "select max(id) from Order";
        Number result = (Number) session.createQuery(hqlSelect).uniqueResult();
        if (result != null) {
            id = result.intValue();
        }

        session.getTransaction().commit();
        session.close();

        return id;
    }


    public void addOrderDetail(int orderID, int bookID, int quantity){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String hql = "insert into OrderDetail (orderID, bookID, quantity) values (:orderID, :bookID, :quantity)";
        session.createQuery(hql)
                .setParameter("orderID", orderID)
                .setParameter("bookID", bookID)
                .setParameter("quantity", quantity)
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public void deleteOrder(int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("delete from Order where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public List<Order> getAllOrdersWithDetails(){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = session.createQuery("from Order o join fetch o.orderDetails", Order.class).list();
            session.getTransaction().commit();
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
