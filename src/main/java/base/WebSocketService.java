package base;

import Users.UserProfile;
import frontend.GameWebSocket;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by surkov on 21.12.14.
 */
public interface WebSocketService {
    //Добавляем сокет в список игроков.
    void addUserSocket(GameWebSocket userSocket);

    void notifyMyLogin(String sessionId, String login, Map<String, String> socketSessions);

    void notifyOtherPlayers(UserProfile initiatedUserProfile, JSONObject output, boolean showMe);

    void fireToEnemy(UserProfile attacker, String victimLogin, int gunId, AccountService accountService);

    void removeSocket(GameWebSocket userSocket);
}
