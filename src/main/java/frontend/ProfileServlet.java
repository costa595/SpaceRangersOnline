package frontend;

import Users.UserProfile;
import Users.AccountService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by К on 07.10.2014.
 */
public class ProfileServlet extends HttpServlet {

    public AccountService accountService;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        int avatar = request.getIntHeader("avatar");
        int race = request.getIntHeader("race");
        float x = request.getIntHeader("x");
        float y = request.getIntHeader("y");

        HttpSession session = request.getSession();

        UserProfile newUserInfo = new UserProfile(login, email, password, avatar, race);
        accountService.updateUser(newUserInfo, session.getId());
    }

    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");

        HttpSession session = request.getSession();

        accountService.removeUser(login, session.getId());
    }
}
