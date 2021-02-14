import com.google.gson.Gson;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        /*Creating Server and Servlets*/

        /*Creating a new Server*/
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        /**/

        /*Assigning port number*/
        connector.setPort(8090);
        /**/

        server.setConnectors(new Connector[]{connector});
        /*Creating a servlet handler*/
        ServletHandler servletHandler = new ServletHandler();
        /**/

        /*Creating servlets*/
        Map<String,String> ipMap = new HashMap<>();
        Map<String,User> userMap = new HashMap<>();
        ServerServlet servers = new ServerServlet(new Gson(), ipMap, userMap);
        UserManager userManager = new UserManager(userMap,ipMap);
        MessageSender messageSender = new MessageSender();
        SubscriptionServlet subscriptionServlet = new SubscriptionServlet(new Gson(), userMap, userManager, messageSender);
        UserList userList = new UserList(ipMap,new Gson());
        PendingMessagesReturnerServlet pendingMessagesReturnerServlet = new PendingMessagesReturnerServlet(new Gson(), userManager);
        servletHandler.addServletWithMapping(new ServletHolder(servers), "/chat");
        servletHandler.addServletWithMapping(new ServletHolder(subscriptionServlet), "/subscribe");
        servletHandler.addServletWithMapping(new ServletHolder(userList), "/list");
        servletHandler.addServletWithMapping(new ServletHolder(pendingMessagesReturnerServlet), "/getPendingMessages");
        server.setHandler(servletHandler);
        /**/

        /*Staring server*/
        /**/
        server.start();
    }

}
