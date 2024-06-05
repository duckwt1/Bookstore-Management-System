package bus;

import dao.OrderDAO;
import dto.Order;
import dto.OrderDetail;

import java.util.List;

public class OrderBus {

    public List<Order> getAllOrders(){
        return new OrderDAO().getAllOrders();
    }

    public List<OrderDetail> getAllOrderDetails(){
        return new OrderDAO().getAllOrderDetails();
    }

    public List<Order> getAllOrderWithDetails(){
       return new OrderDAO().getAllOrdersWithDetails();
    }

    public int addOrder(int cusID){
        return new OrderDAO().addOrder(cusID);
    }

    public void addOrderDetail(int orderID, int bookID, int quantity){
        new OrderDAO().addOrderDetail(orderID, bookID, quantity);
    }
}
