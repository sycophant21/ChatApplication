import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Set;

public class MessageSender {
    public MessageSender() {

    }
    /*Sends a message to multiple users received from a single user*/
    public void sendMessageToManyFromOneUser(User user, Set<String> setOfReceivers, String message, String url) {
        for (String s : setOfReceivers) {
                try {
                    Unirest.post(s + url).body("{\n\"sender\":\"" + user.getUserID() + "\",\n\"message\":\""+message+"\"\n}").asJson();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
        }
    }
    /*Sends message to a single user*/
    public void sendMessageToOne(User recipientUser,String message,String url) {
        try {
            Unirest.post("http://" + recipientUser.getIPAddress() + url).body("\n\"message\":\""+message+"\"\n}").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
