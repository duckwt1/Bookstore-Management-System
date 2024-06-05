package bus;

import dto.Customer;
import dto.Message;

import java.util.List;

public class MessageBus {
    public void saveMessage(String message, int sender, int receiver) {
        new dao.MessageDAO().saveMessage(message, sender, receiver);
    }

    public List<Message> getMessages(Customer sender, Customer receiver) {
        return new dao.MessageDAO().getMessages(sender, receiver);
    }
}
