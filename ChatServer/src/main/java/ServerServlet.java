import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.IOUtils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class ServerServlet extends HttpServlet {
    private final Gson gson;
    private final Map<String, String> ipMap;
    private final Map<String,User> userMap;
    public ServerServlet(Gson gson, Map<String, String> ipMap, Map<String, User> userMap) {
        this.gson = gson;
        this.ipMap = ipMap;
        this.userMap = userMap;
    }

    /*Operates as a middleMan in a conversation*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        MessageManager messageManager = gson.fromJson(message, MessageManager.class);
        try {
            if (ipMap.containsKey(messageManager.getReceiver())) {
                String to = ipMap.get(messageManager.getReceiver());
                String from = ipMap.get(messageManager.getSender());
                if (!to.equals(from)) {
                    if (Unirest.post(to + "/getMessage").body("{\n\"sender\":\"" + messageManager.getSender() + "\",\n\"message\":\"" + messageManager.getMessage() + "\"\n}").asJson().getStatus() == 200) {
                        response.getWriter().println(gson.toJson(new Status("Message Sent")));
                    }
                }
                else {
                    response.getWriter().println(gson.toJson(new Status("You can't send message to yourself")));
                }
            } else {
                response.getWriter().println(gson.toJson(new Status("User does not exist")));
            }
        } catch (UnirestException e) {
            String to = messageManager.getReceiver();
            userMap.get(to).insertMessages(messageManager);
            response.getWriter().println(gson.toJson(new Status("Message not sent, User offline")));
        }
    }
}