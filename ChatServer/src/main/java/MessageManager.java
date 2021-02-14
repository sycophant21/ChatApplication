import java.util.Date;

public class MessageManager {
    private final String sender;
    private final String message;
    private final String receiver;
    private final Date date;

    public MessageManager(String sender, String receiver, String message, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getDate() {
        return date;
    }
}
