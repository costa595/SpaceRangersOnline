package frontend;

import Users.AccountService;
import Users.UserProfile;
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

/**
 * Created by Андрей on 19.11.2014.
 */
@WebSocket
public class GameWebSocket {
    private UserProfile userProfile;
    private Session session;
    private String sessionId; //Сессия сокета, не всегда эквивалентна веб-сессии
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
            final String name = userProfile.getLogin();
            this.userProfile = userProfile;
        }

        this.webSocketService = webSocketService;
    }

    public String getSessionId(){
        return sessionId;
    }

    public String getName() {
        return userProfile.getLogin();
    }

    public int getLocation(){
        return userProfile.getLocationId();
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
                    String possibleUserLogin = possibleUser.getLogin();
                    System.out.println("userProfile "+userProfile+" "+possibleUser);
                    if (userProfile == null) {
                        userProfile = possibleUser;
                        return false;
                    } else {
                        String curUserLogin = userProfile.getLogin();
                        if (possibleUserLogin.equals(curUserLogin)) {
                            userProfile = possibleUser;
                            return false;
                        } else {
                            return true;
                        }
                    }

                } else {
                    return true;
                }
            } else {
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
        System.out.println("onMessage - " + data.toString());
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
                System.out.println("notify Other players");
                webSocketService.notifyOtherPlayers(this.userProfile, outputOthers);
                break;
        }

    }

    private void updateMyCoords(JSONObject json){
        JSONObject output = new JSONObject();
        final float x, y;
        try {
            x = Float.parseFloat(json.get("x").toString());
            y = Float.parseFloat(json.get("y").toString());
        } catch (Exception e){
            parseJSONErrorSend(e);
            return;
        }
        userProfile.updateCoords(x, y);
        //Уведомляем других игроков об изменении своих координат
        JSONObject outputOthers = new JSONObject();
        outputOthers.put("login", userProfile.getLogin());
        outputOthers.put("type", "coordsNotification");
        outputOthers.put("x", x);
        outputOthers.put("y", y);
        outputOthers.put("prevX", userProfile.getPrevX());
        outputOthers.put("prevY", userProfile.getPrevY());
        webSocketService.notifyOtherPlayers(userProfile, outputOthers);
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
        //accountService.sessions.keySet() заменена на users для того, чтобы тестировать коллекции других игроков
        for (String key : accountService.sessions.keySet()) {
            UserProfile otherUser = accountService.sessions.get(key);
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
        System.out.println("showOtherShips output "+output.toString());
        sendJSONObject(output);
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
        System.out.println("Error JSON "+e.toString());
        output.put("type", "error");
        output.put("error", "parsingJson");
        output.put("exception", e.toString());
        sendJSONObject(output);
    }

    //Колбек на открытие сокета
    @OnWebSocketConnect
    public void onOpen(Session session) {
        System.out.println("Socket Opened "+sessionId);
        this.session = session;
        webSocketService.addUserSocket(this);
    }

    //Закрытие сокета (колбек)
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        webSocketService.removeSocket(this);
        System.out.println("onClose Socket");
    }
}
