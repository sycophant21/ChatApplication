import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class PendingMessagesReturnerServlet extends HttpServlet {
    private final Gson gson;
    private final UserManager userManager;

    public PendingMessagesReturnerServlet(Gson gson, UserManager userManager) {
        this.gson = gson;
        this.userManager = userManager;
    }

    /*Returns pending messages stored corresponding to the requested user*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userManager.getUser(gson.fromJson(IOUtils.toString(request.getInputStream(), Charset.defaultCharset()), User.class).getUserID());
        List<MessageManager> messageManagerList = new ArrayList<>();
        if (user != null) {
            if (user.getNumberOfPendingMessages() != 0) {
                Queue<MessageManager> messageManagerQueue = user.getMessageList();
                messageManagerList.addAll(messageManagerQueue);
                response.getWriter().println(gson.toJson(messageManagerList));
                messageManagerQueue.clear();
            } else {
                response.getWriter().println(gson.toJson(new Status("No new messages")));
            }
        } else {
            response.getWriter().println(gson.toJson(new Status("You are not a registered user")));
        }
    }
}
