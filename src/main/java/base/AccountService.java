package base;

import Users.UserProfile;
import constants.CodeResponses;
import profiles.ItemProfile;
import profiles.MobProfile;
import profiles.SystemProfile;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by surkov on 11.12.14.
 */
public interface AccountService {
    public CodeResponses login(String login, String password, String sessionId) throws SQLException;
    public Map<Integer, MobProfile> getMobs();
    public Map <Integer, SystemProfile> getSystems();
    public Map<Integer, ItemProfile> getItems();
    public Map<String, String> getSocketSessions();
    public Map<String, UserProfile> getSessions();
    public UserProfile getCurrentUser(String sessionId);
    public SystemProfile getCurrentSystem(int id);
    public ItemProfile getItem(int id);
    public Map<String, UserProfile> getUsers();
    public int countUsers ();
    public CodeResponses register(UserProfile profile) throws SQLException;
    public CodeResponses logout(String sessionId);
    public int countItems ();
    public int countReg ();
    public int countSystems ();
    public void updateUser(UserProfile userNewInfo, String sessionID);
    public void setSocketSessions(Map<String, String> socketSessions);
    public void updateUsersSession (String oldSessionId, String newSessionId);
}
