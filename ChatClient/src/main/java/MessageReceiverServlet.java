import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

public class MessageReceiverServlet extends HttpServlet {
    private final Gson gson;

    public MessageReceiverServlet(Gson gson) {
        this.gson = gson;
    }

    /*Servlet to receive messages directed to this particular user*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = IOUtils.toString(request.getInputStream(), Charset.defaultCharset());
        Message messageDetails = gson.fromJson(message, Message.class);
        if (messageDetails.getSender() != null) {
            System.out.println(messageDetails.getSender() + ":\t" + messageDetails.getMessage());
        }
        else {
            System.out.println(messageDetails.getMessage());
        }
        response.setStatus(200);
    }
}
