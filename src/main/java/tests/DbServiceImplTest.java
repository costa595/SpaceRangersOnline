package tests;

import Users.UserProfile;
import Users.UserProfileImpl;
import base.DBService;
import base.DbServiceImpl;
import junit.framework.TestCase;
import table.Player;

public class DbServiceImplTest extends TestCase {
    private UserProfile profile = new UserProfileImpl("test12345", "test1@test.ru", "test", 1, 1);

    private DBService dbService;

    public void testDb () throws Exception {
        dbService = new DbServiceImpl("hibernate_test.cfg.xml");
        Player player = new Player(profile);
        dbService.addPlayer(player);
        Player player_test = dbService.getPlayerByLogin("test12345");
        assertEquals(player_test, player);
    }

}