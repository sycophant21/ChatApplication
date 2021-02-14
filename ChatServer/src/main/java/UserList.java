import com.google.gson.Gson;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserList extends HttpServlet {
    private final Map<String, String> ipMap;
    private final Gson gson;
    public UserList(Map<String, String> ipMap, Gson gson) {
        this.gson = gson;
        this.ipMap = ipMap;
    }

    /*Returns the list of available users (Online users)*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<String> usersSet = new HashSet<>();
        for (String s : ipMap.keySet()) {
            if (!s.equals(ipMap.get(request.getRemoteUser()))) {
                usersSet.add(s);
            }
        }
        response.getWriter().println(gson.toJson(usersSet));
    }
}
