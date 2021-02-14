import java.util.Objects;
import java.util.Queue;

public class User {
    private final String userID;
    private final String IPAddress;
    private final Queue<MessageManager> messageList;

    public User(String userID, String IPAddress, Queue<MessageManager> messageList) {
        this.userID = userID;
        this.IPAddress = IPAddress;
        this.messageList = messageList;
    }

    public String getUserID() {
        return userID;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public int getNumberOfPendingMessages() {
        return messageList.size();
    }

    public Queue<MessageManager> getMessageList() {
        return messageList;
    }

    public void insertMessages(MessageManager message) {
        messageList.add(message);
    }

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return Objects.equals(getUserID(), user.getUserID()) &&
                Objects.equals(getIPAddress(), user.getIPAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserID(), getIPAddress());
    }
}
