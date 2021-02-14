import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONArray;

import java.util.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        /*Creating Server and Servlets*/

        /*Creating a new Server*/
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        /**/

        /*Assigning port number*/
        int portNumber = Integer.parseInt(args[0].substring(args[0].indexOf(':') + 1));
        String clientAddress = "http://127.0.0.1:" + portNumber;
        connector.setPort(portNumber);
        server.setConnectors(new Connector[]{connector});
        /**/

        /*Creating a servlets handler*/
        ServletHandler servletHandler = new ServletHandler();
        /**/

        /*Creating new servlet objects*/
        MessageReceiverServlet messageReceiver = new MessageReceiverServlet(new Gson());
        String serverAddress = "http://127.0.0.1:8090";
        MessageSenderServlet messageSender = new MessageSenderServlet(new Gson(), clientAddress, args[1], serverAddress);
        ClientRegistrarServlet clientRegistrar = new ClientRegistrarServlet(new Gson(), clientAddress, args[1], serverAddress);
        servletHandler.addServletWithMapping(new ServletHolder(messageReceiver), "/getMessage");
        server.setHandler(servletHandler);
        /**/

        /**/

        /*Register User*/
        Status liveStatus = clientRegistrar.registerUser();
        /**/
        /*Get pending messages*/
        String pendingMessageStatus = messageSender.getPendingMessages(liveStatus);
        /**/

        /*Starting server*/
        server.start();
        /**/

        System.out.println(pendingMessageStatus);

        while (true) {

            System.out.println("Type 'AU' for list of available users or 'SM' to send a message");
            String whatToDo = sc.nextLine();

            /*Gets list of available users*/
            if (whatToDo.equalsIgnoreCase("AU")) {
                JSONArray jsonArray = Unirest.get( serverAddress + "/list").asJson().getBody().getArray();
                List<String> userSet = new ArrayList<>();
                for (Object o : jsonArray) {
                    String s = (String) o;
                    if (!s.equals(args[1])) {
                        userSet.add(s);
                    }
                }
                System.out.println(userSet);
                /**/

            } else if(whatToDo.equalsIgnoreCase("SM"))  {
                /*Sends message to a user*/
                System.out.print("To: ");
                String to = sc.nextLine();
                System.out.print("Message: ");
                String message = sc.nextLine();
                String messageSent = messageSender.sendMessage(to, message);
                /**/
                /*Prints the message*/
                System.out.println(messageSent);
                /**/
            }
            else {
                System.out.println("Invalid command, try again");
            }
        }
    }
}
