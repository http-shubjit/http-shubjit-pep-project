package Service;




import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;
public class MessageService {

    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(int postedBy, String messageText, long timePostedEpoch) throws SQLException {
        if (messageText == "" ||  messageText.length() > 255|| postedBy!=1) {
            return null;
        }
       
        return messageDAO.createMessage(postedBy, messageText, timePostedEpoch);
    }

    public Message deleteMessage(int messageId) throws SQLException {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessage(int message_id, String message_text) throws SQLException {
        if (message_id <= 0 || message_text == "" || message_text.length() > 255) {
            return null;
        }
        return messageDAO.updateMessage(message_id, message_text);
    }

    public List<Message> getMessagesByUserId(int userId) throws SQLException {
        return messageDAO.getMessagesByUserId(userId);
    }

    public Message getMessageById(int messageId) throws SQLException {
        return messageDAO.getMessageById(messageId);
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

   

   
}


