package tests;

import Users.UserProfile;
import Users.UserProfileImpl;
import base.AccountService;
import constants.CodeResponses;
import junit.framework.TestCase;
import main.ConstructFactory;

import static org.mockito.Mockito.mock;

public class AccountServiceImplTest extends TestCase {

    ConstructFactory factory = new ConstructFactory();
    AccountService accountService = factory.getAccountService(factory.getDBService());
    UserProfile profile = new UserProfileImpl("test1", "test1@test.ru", "test", 1, 1);
    protected void setUp() throws Exception {

    }

    public void testReg() throws Exception {
        CodeResponses responses = accountService.register(profile);
        assertEquals(responses, CodeResponses.OK);
        responses = accountService.login("test1", "test", "123");
        assertEquals(responses, CodeResponses.OK);
    }
}