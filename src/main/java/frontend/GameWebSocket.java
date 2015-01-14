package frontend;

import Users.UserProfile;
import base.AccountService;
import base.WebSocketService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Date;
import java.util.Map;

/**
 * Created by Андрей on 19.11.2014.
 */
@WebSocket
public class GameWebSocket {
    private UserProfile userProfile;
    private boolean profileInited = false;
    private Session session;
    private String sessionId; //Сессия сокета, не эквивалентна веб-сессии
    private AccountService accountService;
    //    private GameSession gameSession;
    private WebSocketService webSocketService;

    //Конструктор
    public GameWebSocket(String sessionId, WebSocketService webSocketService, AccountService accountService) {
        this.accountService = accountService;
        this.sessionId = sessionId;
        final UserProfile userProfile = accountService.getCurrentUser(sessionId);
        System.out.println("GameWebSocket: UserProfile "+userProfile);
        if (userProfile != null) {
            this.userProfile = userProfile;
            String profileLogin = userProfile.getLogin();
            Map<String, String> socketSessions = accountService.getSocketSessions();
            webSocketService.notifyMyLogin(sessionId, profileLogin, socketSessions);
            accountService.setSocketSessions(socketSessions);
            this.profileInited = true;
        }

        this.webSocketService = webSocketService;
    }

    public String getSessionId(){
        return sessionId;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public String getName() {
        if (userProfile != null) {
            return userProfile.getLogin();
        } else {
            return "";
        }
    }

    public int getLocation(){
        if (userProfile != null) {
            return userProfile.getLocationId();
        } else {
            return 0;
        }
    }

    public void sendJSONObject(JSONObject object) {
        try {
            session.getRemote().sendString(object.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    private void sendJSONArray(JSONArray object) {
        try {
            session.getRemote().sendString(object.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    //Проверка на валидность сессии в сокете
    private boolean checkForDifferentSessionId(String receivedSessionId){
        if (userProfile == null) {
            userProfile = accountService.getCurrentUser(sessionId);
            if (userProfile == null) {
                UserProfile possibleUser = accountService.getCurrentUser(receivedSessionId);
                if (possibleUser != null) {
//                    String possibleUserLogin = possibleUser.getLogin();
//                    System.out.println("userProfile "+userProfile+" "+possibleUser);
                    userProfile = possibleUser;
                    accountService.updateUsersSession(receivedSessionId, sessionId);
                    if (!this.profileInited) {
                        Map<String, String> socketSessions = accountService.getSocketSessions();
                        webSocketService.notifyMyLogin(sessionId, userProfile.getLogin(), socketSessions);
                        this.profileInited = true;
                    }
                    return false;

                } else {
                    return true;
                }
            } else {
                if (!this.profileInited) {
                    Map<String, String> socketSessions = accountService.getSocketSessions();
                    webSocketService.notifyMyLogin(sessionId, userProfile.getLogin(), socketSessions);
                    this.profileInited = true;
                }
                return false;
            }
        } else {
            return false;
        }
    }

    //Обработчик получения сообщений от клиента
    @OnWebSocketMessage
    public void onMessage(String data) throws ParseException {
        final JSONObject json;
        String receivedSessionId, messageType;
        JSONObject output = new JSONObject();
//        System.out.println("onMessage - " + data.toString());
        try {
            json = (JSONObject) new JSONParser().parse(data);
            receivedSessionId = json.get("sessionId").toString();
            messageType = json.get("type").toString();
        }catch (Exception e){
            parseJSONErrorSend(e);
            return;
        }

        if (checkForDifferentSessionId(receivedSessionId)){
            sessionErrorSend();
            return;
        }

        switch (messageType){
            case "updateCoords":
                updateMyCoords(json);
                break;
            case "giveOtherShipsCoords":
                showOtherShips();
                JSONObject outputOthers = makeNewPlayerNotification(this.userProfile);
//                System.out.println("notify Other players");
                webSocketService.notifyOtherPlayers(this.userProfile, outputOthers, false);
                break;
            case "fireToEnemy":
                fireToEnemy(json);
                break;
        }

    }

    private void updateMyCoords(JSONObject json){
        JSONObject output = new JSONObject();
        final float x, y, prevX, prevY;
        try {
            x = Float.parseFloat(json.get("x").toString());
            y = Float.parseFloat(json.get("y").toString());
            prevX = Float.parseFloat(json.get("curX").toString());
            prevY = Float.parseFloat(json.get("curY").toString());
        } catch (Exception e){
            parseJSONErrorSend(e);
            return;
        }
        userProfile.updateCoords(x, y, prevX, prevY);
        //Уведомляем других игроков об изменении своих координат
        JSONObject outputOthers = new JSONObject();
        outputOthers.put("login", userProfile.getLogin());
        outputOthers.put("type", "coordsNotification");
        outputOthers.put("x", x);
        outputOthers.put("y", y);
        outputOthers.put("prevX", userProfile.getPrevX());
        outputOthers.put("prevY", userProfile.getPrevY());
        webSocketService.notifyOtherPlayers(userProfile, outputOthers, false);
//        notifyOtherPlayers(outputOthers);
        output.put("type", "OK");
        sendJSONObject(output);
    }

    private JSONObject makeNewPlayerNotification (UserProfile user){
        JSONObject output = new JSONObject();
        Date date = new Date();
        output.put("type", "otherShips");
        JSONArray ships = new JSONArray();
        JSONObject ship = new JSONObject();
        ship.put("login", user.getLogin());
        ship.put("x", user.getX());
        ship.put("y", user.getY());
        ship.put("prevX", user.getPrevX());
        ship.put("prevY", user.getPrevY());
        long timeLeft = date.getTime() - user.getLastActionTime();
        ship.put("timeLeft", timeLeft);
        ships.add(ship);
        output.put("ships", ships);
//        sendJSONObject(output);
        return output;
    }

    //Функция показывает другие корабли в этой системе
    private void showOtherShips(){
        JSONObject output = new JSONObject();
        output.put("type", "otherShips");
        JSONArray ships = new JSONArray();
        Date date = new Date();
        for (String key :  accountService.getSessions().keySet()) {
            UserProfile otherUser = accountService.getSessions().get(key);
            if (userProfile.getLocationId() == otherUser.getLocationId() && !userProfile.getLogin().equals(otherUser.getLogin())) {
                JSONObject ship = new JSONObject();
                ship.put("login", otherUser.getLogin());
                ship.put("x", otherUser.getX());
                ship.put("y", otherUser.getY());
                ship.put("prevX", otherUser.getPrevX());
                ship.put("prevY", otherUser.getPrevY());
                long timeLeft = date.getTime() - otherUser.getLastActionTime();
                ship.put("timeLeft", timeLeft);
                ships.add(ship);
            }
        }
        output.put("ships", ships);
//        System.out.println("showOtherShips output "+output.toString());
        sendJSONObject(output);
    }

    private void fireToEnemy(JSONObject json){
        String victimLogin = "";
        int gunId = 0;
        try {
            victimLogin = json.get("victimLogin").toString();
            gunId = Integer.parseInt(json.get("gunId").toString());
        } catch (Exception e){
            parseJSONErrorSend(e);
            return;
        }
        webSocketService.fireToEnemy(userProfile, victimLogin, gunId, accountService);
    }

    //Отправить ошибку о проблемах с сессией
    private void sessionErrorSend(){
        JSONObject output = new JSONObject();
        output.put("type", "error");
        output.put("error", "no-session");
        sendJSONObject(output);
    }

    //Отправить ошибку о проблеме с парсингом полученного запроса
    private void parseJSONErrorSend(Exception e){
        JSONObject output = new JSONObject();
//        System.out.println("Error JSON "+e.toString());
        output.put("type", "error");
        output.put("error", "parsingJson");
        output.put("exception", e.toString());
        sendJSONObject(output);
    }

    //Колбек на открытие сокета
    @OnWebSocketConnect
    public void onOpen(Session session) {
//        System.out.println("Socket Opened "+sessionId);
        this.session = session;
        webSocketService.addUserSocket(this);
    }

    //Закрытие сокета (колбек)
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        webSocketService.removeSocket(this);
//        System.out.println("onClose Socket");
    }
}
