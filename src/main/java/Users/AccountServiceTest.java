package Users;

import base.PlayerDao;
import constants.CodeResponses;
import junit.framework.TestCase;
import table.Player;

import static org.mockito.Mockito.*;

public class AccountServiceTest extends TestCase {
    PlayerDao playerDao = mock(PlayerDao.class);

    AccountService accountService = new AccountService(playerDao);

    @Override
    protected void setUp() throws Exception {

    }

    public void testLogin() throws Exception {
        when(playerDao.getPlayerByLogin("log")).thenReturn(new Player());

        CodeResponses responses = accountService.login("log", "psw", "123");

        assertEquals(responses, CodeResponses.OK);
    }
}