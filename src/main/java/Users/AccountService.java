package Users;

//import constants.CodeResponses;

import constants.CodeResponses;
import dao.PlayerDao;
import profiles.ItemProfile;
import profiles.SystemProfile;
import table.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmitry on 13.09.14.
 */
public class AccountService {
    public Map<String, UserProfile> sessions = new HashMap<>();
    public Map<String, UserProfile> users = new HashMap<>();
    public Map<String, String> loginSessions = new HashMap<>(); //Для предотвразение дублирования суссий с одноим и тем же логином. Хранится: логин - id сессии, описк оп логину
    public Map<String, SystemProfile> systems = new HashMap<>();
    public Map<String, ItemProfile> items = new HashMap<>();

    PlayerDao playerDao;

    public AccountService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }
    public CodeResponses register(UserProfile profile) throws SQLException {

        String login = profile.getLogin();

        if (this.users.get(login) != null) {
            return CodeResponses.REGISTRED;
        }
        else {
            Player player = new Player();
            player.setLogin(login);
            player.setPass(profile.getPassword());
            player.setMail(profile.getEmail());
            player.setAdmin(0);
            playerDao.addPlayer(player);
            this.users.put(login, profile);
            System.out.println(this.users.toString());
            return CodeResponses.OK;
        }
    }

    public CodeResponses login(String login, String password, String sessionId) {

        System.out.println("login "+this.users.toString());

//        Player currentUser1;
//        try {
//            currentUser1 = playerDao.getPlayerByLogin(login);
//            System.out.println(login);
//            UserProfile profile = new UserProfile(currentUser1);
//            users.put(login, profile);
//        } catch (Exception e) {};

//        UserProfile currentUser1 = playerDao.getPlayerByLogin(login);

        UserProfile currentUser = this.users.get(login);
        if ((currentUser != null) && (password.equals(currentUser.getPassword()))) {
            String tmpSessionId = this.loginSessions.get(login);
            if (tmpSessionId != null) {
                this.sessions.remove(tmpSessionId);
            }
            currentUser.updateCoords(0, 0);
            currentUser.updateCoords(0, 0);
            this.sessions.put(sessionId, currentUser);
            this.loginSessions.put(login, sessionId);
            return CodeResponses.OK;
        }
        else {
            return CodeResponses.NOT_LOGINED;
        }
    }

    public void updateUsersSession (String oldSessionId, String newSessionId) {
        UserProfile curUser = this.sessions.get(oldSessionId);
        this.sessions.remove(oldSessionId);
        this.sessions.put(newSessionId, curUser);
    }

    public UserProfile getCurrentUser(String sessionId) {
        System.out.println("sessions "+this.sessions.toString());
        return this.sessions.get(sessionId);
    }

    public SystemProfile getCurrentSystem(String id) {
        return  this.systems.get(id);
    }
    public ItemProfile getItem(String id) {
        System.out.println("this.items "+this.items.toString());
        return  this.items.get(id);
    }

    public void updateUser(UserProfile userNewInfo, String sessionID) {

        this.users.remove(userNewInfo.getLogin());
        this.users.put(userNewInfo.getLogin(), userNewInfo);

        this.sessions.remove(sessionID);
        this.sessions.put(sessionID, userNewInfo);
    }

    public void removeUser(String login, String sessionID) {

        this.users.remove(login);

        this.sessions.remove(sessionID);
    }

    public CodeResponses logout(String sessionId) {
        sessions.remove(sessionId);

        return CodeResponses.OK;
    }
    public int countUsers () { return sessions.size(); }
    public int countReg () { return users.size(); }
    public void saveToBd() {
//        for (Map.Entry<String, UserProfile> entry : users.entrySet()) {
//            System.out.println("login > "+ entry.getValue().getLogin()+
//                    " pass > " + entry.getValue().getPassword() +
//                    " key > " + entry.getKey());
//        }
    }
}
