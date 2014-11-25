package admin;

import Users.AccountService;
import main.TimeHelper;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 21.09.2014.
 */
public class AdminPageServlet extends HttpServlet {
    AccountService accountService;
    public static final String adminPageURL = "/admin";

    public AdminPageServlet(AccountService accountService) { this.accountService = accountService;}

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
        pageVariables.put("status", "run");
        int online = accountService.countUsers();
        int registered = accountService.countReg();
        pageVariables.put("registered", registered);
        pageVariables.put("online", online);
        response.getWriter().println(PageGenerator.getPage("admin.tml", pageVariables));
    }
}
