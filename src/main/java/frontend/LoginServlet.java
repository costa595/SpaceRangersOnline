package frontend;

import Users.AccountService;
import Users.UserProfile;
import constants.CodeResponses;
import org.json.simple.JSONObject;
import profiles.SystemProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by dmitry on 14.09.14.
 */
public class LoginServlet extends HttpServlet {

    public AccountService accountService;

    public LoginServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        UserProfile currentUser = accountService.getCurrentUser(sessionId);
        String sysId = null;
        String engineId = null;
        String shipId = null;
        String fuelId = null;
        try {
            sysId = new Integer(currentUser.getLocationId()).toString();
            engineId = new Integer(currentUser.getEngine()).toString();
            shipId = new Integer(currentUser.getShip()).toString();
            fuelId = new Integer(currentUser.getFuel()).toString();
        } catch (Exception e) {}
        SystemProfile currentSystem = accountService.getCurrentSystem(sysId);
        //ItemProfile curItem = accountService.getItem();
        if (currentUser != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject output = new JSONObject();
            JSONObject system = new JSONObject();
            JSONObject engine = new JSONObject();
            JSONObject ship = new JSONObject();
            JSONObject fuel = new JSONObject();

            fuel.put("id", accountService.getItem(fuelId).getId());
            fuel.put("name", accountService.getItem(fuelId).getName());
            fuel.put("capacity", accountService.getItem(fuelId).getP1());
            fuel.put("price", accountService.getItem(fuelId).getP2());
            fuel.put("durability", accountService.getItem(fuelId).getP3());
            ship.put("id", accountService.getItem(shipId).getId());
            ship.put("name", accountService.getItem(shipId).getName());
            ship.put("hp", accountService.getItem(shipId).getP1());
            ship.put("block", accountService.getItem(shipId).getP2());
            ship.put("price", accountService.getItem(shipId).getP3());
            engine.put("id", accountService.getItem(engineId).getId());
            engine.put("name", accountService.getItem(engineId).getName());
            engine.put("speed", accountService.getItem(engineId).getP1());
            engine.put("giper", accountService.getItem(engineId).getP2());
            engine.put("price", accountService.getItem(engineId).getP3());
            engine.put("durability", accountService.getItem(engineId).getP4());
            system.put("id", currentSystem.getId());
            system.put("name", currentSystem.getName());
            system.put("star", currentSystem.getStar());
            system.put("background", currentSystem.getBackground());

            output.put("fuel", fuel);
            output.put("ship", ship);
            output.put("engine", engine);
            output.put("system", system);
            output.put("login", currentUser.getLogin());
            output.put("email", currentUser.getEmail());
            output.put("avatar", currentUser.getAvatar());
            output.put("sessionId", sessionId);
            output.put("race", currentUser.getRace());
            output.put("x", currentUser.getX());
            output.put("y", currentUser.getY());
            output.put("rank", currentUser.getRank());
            output.put("cr", currentUser.getCr());
            output.put("kr", currentUser.getKr());
            output.put("w1", currentUser.getW1());
            output.put("w2", currentUser.getW2());
            output.put("w3", currentUser.getW3());
            output.put("w4", currentUser.getW4());
            output.put("w5", currentUser.getW5());
            output.put("droid", currentUser.getDroid());
            output.put("hook", currentUser.getHook());
            output.put("generator", currentUser.getGenerator());
            output.put("radar", currentUser.getRadar());
            output.put("scaner", currentUser.getScaner());



            response.setHeader("Content-type", "application/json");

            response.getWriter().println(output);
        }
        else {
            response.setStatus(HttpServletResponse.SC_OK);
            JSONObject output = new JSONObject();
            response.getWriter().println(output);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);// перенести вниз, тк не знаем ок или неок
        HttpSession session = request.getSession();

        JSONObject output = new JSONObject();
        if (accountService.login(login, password, session.getId()) == CodeResponses.OK) {
            String sessionId = session.getId();
            UserProfile currentProfile = accountService.getCurrentUser(session.getId());
            output.put("status", 200);
            output.put("login", currentProfile.getLogin());
            output.put("email", currentProfile.getEmail());
            output.put("avatar", currentProfile.getAvatar());
            output.put("sessionId", sessionId);
        }
        else {
            output.put("status", 403);
        }
        response.setHeader("Content-type", "application/json");

        response.getWriter().println(output);
    }
}
