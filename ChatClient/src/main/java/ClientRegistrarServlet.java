import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;

public class ClientRegistrarServlet {
    private final Gson gson;
    private final String IPAddress;
    private final String userID;
    private final String serverAddress;
    public ClientRegistrarServlet(Gson gson, String IPAddress, String userID, String serverAddress) {
        this.gson = gson;
        this.IPAddress = IPAddress;
        this.userID = userID;
        this.serverAddress = serverAddress;
    }

    /*Registers the user on the server whenever the programme restarts as to let the server know if it's a new user or an old user
    * who came back online again, to include it's name in the online users list*/

    public Status registerUser() {
        Status status;
        try {
            String statusString = Unirest.put(serverAddress + "/subscribe").body("{\n\"userID\":\""+this.userID+"\",\n\"IPAddress\":\""+this.IPAddress+"\"\n}").asJson().getBody().toString();
            status = gson.fromJson(statusString, Status.class);
            System.out.println(status.getStatus());
            return status;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
