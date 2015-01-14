
package frontend;

import base.AccountService;
import constants.CodeResponses;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Created by К on 27.09.2014.
 */
public class LogoutServlet extends HttpServlet{
    public AccountService accountService;

    public LogoutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        JSONObject output = new JSONObject();

        if (accountService.logout(session.getId()) == CodeResponses.OK) {
            output.put("status", 200);
        } else {
            output.put("status", 404);
        }
        response.setHeader("Content-type", "application/json");

        response.getWriter().println(output);
    }
}
