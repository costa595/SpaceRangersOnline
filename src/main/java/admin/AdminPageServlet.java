package admin;

import Users.UserProfile;
import base.AccountService;
import base.DBService;
import main.TimeHelper;
import table.Player;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 21.09.2014.
 */
public class AdminPageServlet extends HttpServlet {
    private AccountService accountService;
    private DBService dbService;

    public AdminPageServlet(AccountService accountService, DBService dbService) {
        this.accountService = accountService;
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        UserProfile currentUser = accountService.getCurrentUser(sessionId);
        boolean isAdmin = false;
        if(currentUser != null) {
            if (currentUser.getAdmin() > 0) {
                isAdmin = true;
            }
        }
        //if(isAdmin) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, Object> pageVariables = new HashMap<>();
            String timeString = request.getParameter("shutdown");
            if (timeString != null) {
                shutdown(timeString);
            }
            String banLogin = request.getParameter("ban");
            if(banLogin != null) {
                try {
                    Player player = dbService.getPlayerByLogin(banLogin);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.print("User " + banLogin + " banned");
            }
            pageVariables.put("status", "run");
//            String online = null;
//            try {
//                online = dbService.countPlayers();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            int registered = accountService.countReg();
            pageVariables.put("registered", registered);
            pageVariables.put("online", "");
            pageVariables.put("admin", isAdmin);
            response.getWriter().println(PageGenerator.getPage("admin.tml", pageVariables));
        //}
    }
    private void shutdown(String timeString) {
        int timeMS = Integer.valueOf(timeString);
        System.out.print("Server will be down after: "+ timeMS + " ms");
        TimeHelper.sleep(timeMS);
        System.out.print("\nShutdown");
        System.exit(0);
    }
}