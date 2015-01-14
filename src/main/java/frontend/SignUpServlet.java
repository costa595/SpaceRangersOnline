package frontend;

import Users.UserProfile;
import Users.UserProfileImpl;
import base.AccountService;
import constants.CodeResponses;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by dmitry on 13.09.14.
 */
public class SignUpServlet extends HttpServlet {
    public AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int race = 0;
        int avatar = 0;

        response.setStatus(HttpServletResponse.SC_OK);

        UserProfile profile = new UserProfileImpl(login, email, password, avatar, race);
        CodeResponses resp = null;
        try {
            resp = accountService.register(profile);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject output = new JSONObject();

        if (resp == CodeResponses.OK)
            output.put("status", 200);
        else
            output.put("status", 404);

        response.setHeader("Content-type", "application/json");

        response.getWriter().println(output);


    }
}
