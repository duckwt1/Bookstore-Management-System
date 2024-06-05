package bus;

import dao.UserDAO;
import dto.Customer;

import java.util.List;

public class UserBus {
    public boolean login(String username, String password){
        return username.equals("admin") && password.equals("admin");
    }

    public List<Customer> getAllCustomers(){
        return new UserDAO().getAllCustomers();
    }

    public boolean register(String username, String email, String password){
        return new  UserDAO().register(username, email, password);
    }

    public void deleteCustomer(int id){
        new UserDAO().deleteCustomer(id);
    }

    public void updateCustomer(Customer customer){
        new UserDAO().updateCustomer(customer);
    }

    public boolean resetPassword(String email, String newPassword){
        return new UserDAO().resetPassword(email, newPassword);
    }
}
