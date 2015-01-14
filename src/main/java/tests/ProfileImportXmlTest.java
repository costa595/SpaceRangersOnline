package tests;

import base.AccountService;
import junit.framework.TestCase;
import main.ConstructFactory;
import profiles.ProfileImportXml;
import profiles.SystemProfile;

public class ProfileImportXmlTest extends TestCase {
    ConstructFactory factory = new ConstructFactory();
    AccountService accountService = factory.getAccountService(factory.getDBService());
    private String path = "src/main/resources/test/systems.xml";
    public void testImportXml() throws Exception {
        assertTrue(accountService.getSystems().isEmpty());
        ProfileImportXml profileImportXml = new ProfileImportXml(accountService, path);
        profileImportXml.importXml();
        SystemProfile system = new SystemProfile("2", "azaza", "1", "1");
        assertEquals(accountService.countSystems(), 3);

        assertTrue(accountService.getSystems().get(2).equals(system));

        assertEquals(accountService.getSystems().get(2).getName(), system.getName());
        assertEquals(accountService.getSystems().get(2).getId(), system.getId());
        assertEquals(accountService.getSystems().get(2).getBackground(), system.getBackground());
        assertEquals(accountService.getSystems().get(2).getStar(), system.getStar());
    }
}