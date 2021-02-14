import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubscriptionServlet extends HttpServlet {
    private final Gson gson;
    private final Map<String,User> userMap;
    private final UserManager userManager;
    private final MessageSender messageSender;
    public SubscriptionServlet(Gson gson, Map<String, User> userMap, UserManager userManager, MessageSender messageSender) {
        this.gson = gson;
        this.userMap = userMap;
        this.userManager = userManager;
        this.messageSender = messageSender;
    }

    /*The user calls this servlet to get registered in the list of registered Users*/
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String registerDetails = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        User user = gson.fromJson(registerDetails, User.class);
        if (userManager.upsertUser(user)){
            response.getWriter().println(gson.toJson(new Status("Back Online")));
        }
        else {
            Set<String> recipientUsersSet = new HashSet<>();
            for (String s : userMap.keySet()) {
                if (!s.equals(user.getUserID())) {
                    recipientUsersSet.add(userMap.get(s).getIPAddress());
                }
            }
            messageSender.sendMessageToManyFromOneUser(user,recipientUsersSet,"I have joined the chat","/getMessage");
            response.getWriter().println(gson.toJson(new Status("Successfully Connected")));
        }

    }
}
