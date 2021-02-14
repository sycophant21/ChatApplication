import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import java.util.List;

public class MessageSenderServlet {
    private final Gson gson;
    private final String IPAddress;
    private final String userID;
    private final String serverAddress;

    public MessageSenderServlet(Gson gson, String IPAddress, String userID, String serverAddress) {
        this.gson = gson;
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.serverAddress = serverAddress;
    }

    /*Takes a string as an argument which contains the name of the user to which the message has to be delivered
    and the message which is to be sent*/

    public String sendMessage(String to, String message) {
        Message messageToBeSent = new Message(userID, to, message);
        String messageString = gson.toJson(messageToBeSent);
        String messageStatus = "";
        Status status = new Status("");
        try {
            messageStatus = Unirest.post(serverAddress+"/chat").body(messageString).asJson().getBody().toString();
            status = gson.fromJson(messageStatus, Status.class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (status.getStatus().equalsIgnoreCase("Message not sent")) {
            return status.getStatus();
        }
        else if(status.getStatus().equalsIgnoreCase("You can't send message to yourself")) {
            return status.getStatus();
        }
        else {
            messageString = userID + " -> " + messageToBeSent.getReceiver() + " :\t " + messageToBeSent.getMessage();
            return messageString;
        }
    }

    /*Gets Pending messages which were sent to the user while he/she was offline (The programme was not running)
    * Takes an argument "liveStatus" which tells it if it's a new user or an old user who restarted the programme*/

    public String getPendingMessages(Status liveStatus) {
        String pendingMessages = "";
        try {
            pendingMessages = Unirest.post(serverAddress + "/getPendingMessages").body("{\n\"userID\":\"" + userID + "\",\n\"IPAddress\":\"" + IPAddress + "\"\n}").asJson().getBody().toString();
                if (pendingMessages.contains("No new messages")) {
                    if (liveStatus.getStatus().equals("Back Online")) {
                        Status status = gson.fromJson(pendingMessages, Status.class);
                        pendingMessages = status.getStatus();
                    }
                    else {
                        pendingMessages = "";
                    }
                } else {
                    JSONArray jsonArray = new JSONArray(pendingMessages);
                    pendingMessages = "";
                    for (int i = 0 ; i < jsonArray.length(); i++) {
                        Message m = gson.fromJson(jsonArray.get(i).toString(), Message.class);
                        pendingMessages = pendingMessages.concat(m.getSender() + ":\t" + m.getMessage() + "\n");
                    }
                }
        }catch (UnirestException e) {
            e.printStackTrace();
        }
        return pendingMessages;
    }

}
