import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UserManager {
    private final Map<String, User> userMap;
    private final Map<String, String> ipMap;

    public UserManager(Map<String, User> userMap, Map<String, String> ipMap) {
        this.userMap = userMap;
        this.ipMap = ipMap;
    }

    /*Upserts an user*/
    public boolean upsertUser(User user) {
        if (!ipMap.containsValue(user.getIPAddress())) {
            ipMap.put(user.getUserID(), user.getIPAddress());
            User user1 = new User(user.getUserID(), user.getIPAddress(), new LinkedBlockingQueue<>());
            userMap.put(user1.getUserID(), user1);
            return false;
        }
        return true;
    }


    public User getUser(String userID) {
        return userMap.get(userID);
    }
}
