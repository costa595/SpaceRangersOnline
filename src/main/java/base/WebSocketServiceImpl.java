package base;

import Users.UserProfile;
import frontend.GameWebSocket;
import mechanics.Fire;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 19.11.2014.
 */
public class WebSocketServiceImpl implements WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();
    private Map<String, String> loginToSession = new HashMap<>(); //login - sessionId

    //Добавляем сокет в список игроков. Теперь знаю, зачем написал
    @Override
    public void addUserSocket(GameWebSocket userSocket) {
        final String sessionId = userSocket.getSessionId();
        userSockets.put(sessionId, userSocket);
    }

    @Override
    public void notifyMyLogin(String sessionId, String login, Map<String, String> socketSessions){
        String curUserOldSession = loginToSession.get(login);
        if (curUserOldSession != null){
            loginToSession.remove(login);
            socketSessions.remove(curUserOldSession);
        }
        String curProfileLogin = socketSessions.get(sessionId);
        if (curProfileLogin == null) {
            socketSessions.put(sessionId, login);
        }
//        System.out.println("notifyMyLogin "+sessionId+" "+login);
        loginToSession.put(login, sessionId);
    }

    @Override
    public void notifyOtherPlayers(UserProfile initiatedUserProfile, JSONObject output, boolean showMe) {
        String userName = initiatedUserProfile.getLogin();
        int userLocationId = initiatedUserProfile.getLocationId();

        for (String key : userSockets.keySet()) {
            GameWebSocket userSocket = userSockets.get(key);
            if (userSocket != null) {
                String otherPlayerUserName = userSocket.getName();
                int otherPlayerLocationId = userSocket.getLocation();

                if ((!userName.equals(otherPlayerUserName) || showMe )&& userLocationId == otherPlayerLocationId){
                    userSocket.sendJSONObject(output);
                }
            }
        }
    }

    @Override
    public void fireToEnemy(UserProfile attacker, String victimLogin, int gunId, AccountService accountService){
        String victimSessionId = this.loginToSession.get(victimLogin);
        UserProfile victimProfile = this.userSockets.get(victimSessionId).getUserProfile();
        if (victimProfile == null)
            return;
        //Работа с потоком
        Fire fire = new Fire(this, accountService, attacker, victimProfile, gunId);
        fire.start();
        //


    }


    @Override
    public void removeSocket(GameWebSocket userSocket) {
        userSockets.remove(userSocket.getSessionId());
    }

}
