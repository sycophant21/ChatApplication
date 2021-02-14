public class Message {
    private final String sender;
    private final String receiver;
    private final String message;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.receiver = "";
    }
    public Message(String sender, String receiver, String message) {
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
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
}
