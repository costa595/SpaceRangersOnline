package base;

import Users.UserProfile;
import frontend.GameWebSocket;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Андрей on 19.11.2014.
 */
public class WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();
//    private Queue<String> waitersQueue = new LinkedList<String>();

    //Добавляем сокет в список игроков. Пока не знаю насчет надобности этого кода
    public void addUserSocket(GameWebSocket userSocket) {

        final String sessionId = userSocket.getSessionId();
        userSockets.put(sessionId, userSocket);

//        if (!waitersQueue.isEmpty()) {
//            final String secondUserName = waitersQueue.remove();
//            if (userName.equals(secondUserName)) {
//                waitersQueue.add(userName);
//                return;
//            }
//            startGame(userName, secondUserName);
//            return;
//        }
//        waitersQueue.add(userName);
    }

    public void notifyOtherPlayers(UserProfile initiatedUserProfile, JSONObject output) {
        System.out.println("notifyOtherPlayers "+output.toJSONString());
        System.out.println("userSockets "+userSockets.toString());
        String userName = initiatedUserProfile.getLogin();
        int userLocationId = initiatedUserProfile.getLocationId();

        for (String key : userSockets.keySet()) {
            GameWebSocket userSocket = userSockets.get(key);
            String otherPlayerUserName = userSocket.getName();
            int otherPlayerLocationId = userSocket.getLocation();

            if (!userName.equals(otherPlayerUserName) && userLocationId == otherPlayerLocationId){
                userSocket.sendJSONObject(output);
            }
        }
    }

    public void removeSocket(GameWebSocket userSocket) {
        userSockets.remove(userSocket.getName());
    }

    private void startGame(String first, String second) {
//        GameSession gameSession = new GameSession(first, second);
//
//        userSockets.get(first).setGameSession(gameSession);
//        userSockets.get(second).setGameSession(gameSession);
//
//        userSockets.get(first).startGame(second);
//        userSockets.get(second).startGame(first);
    }

}
