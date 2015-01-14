package frontend;

import Users.UserProfile;
import base.AccountService;
import base.DBService;
import table.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class ProfileServlet extends HttpServlet {

    public AccountService accountService;
    private DBService dbService;

    public ProfileServlet(AccountService accountService, DBService dbService) {
        this.dbService = dbService;
        this.accountService = accountService;
    }



    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        UserProfile currentUser = accountService.getCurrentUser(sessionId);
        String action = request.getParameter("action");
        String first;
        int firstId = 0;
        String second;
        int secondId = 0;
        System.out.println(action);
        Map<String, Integer> inventory = currentUser.getInventory();

        Player player = new Player(currentUser);

        if(action.equals("swap")) {
            first = request.getParameter("setItem");
            second = request.getParameter("unSetItem");
            System.out.println(first);
            System.out.println(second);
            firstId = currentUser.getInventory().get(first);
            secondId = currentUser.getInventory().get(second);
            System.out.println("!1:"+currentUser.getInventory().get(first));
            inventory.put(first, secondId);
            inventory.put(second, firstId);
            currentUser.setInventory(inventory);
            System.out.println("!2:" + currentUser.getInventory().get(first));
            player.setInventory(currentUser);
            try {
                dbService.updatePlayer(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            accountService.updateUser(currentUser, sessionId);
        }
        if(action.equals("delete")) {
            first = request.getParameter("deletedItem");
            System.out.println("delete: " + first);
            inventory.put(first, 0);
            currentUser.setInventory(inventory);
            player.setInventory(currentUser);
            try {
                dbService.updatePlayer(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            accountService.updateUser(currentUser, sessionId);


        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
