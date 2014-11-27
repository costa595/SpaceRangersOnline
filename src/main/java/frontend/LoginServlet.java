package frontend;

import Users.AccountService;
import Users.UserProfile;
import constants.CodeResponses;
import org.json.simple.JSONObject;
import profiles.ItemProfile;
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
        System.out.println("doGet "+sessionId+" "+currentUser);
        String sysId = null;
        String engineId = null;
        String shipId = null;
        String fuelId = null;
//        String droidId = null;
        try {
            sysId = new Integer(currentUser.getLocationId()).toString();
            engineId = new Integer(currentUser.getEngine()).toString();
            shipId = new Integer(currentUser.getShip()).toString();
            fuelId = new Integer(currentUser.getFuel()).toString();
//            if(currentUser.getDroid() == 0) {
//                accountService.getItem(
//            }
//            droidId = new Integer(currentUser.getDroid()).toString();
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
//            JSONObject droid = new JSONObject();

            ItemProfile curFuel = accountService.getItem(fuelId);

            if (curFuel != null) {
                fuel.put("id", curFuel.getId());
                fuel.put("name", curFuel.getName());
                fuel.put("capacity", curFuel.getP1());
                fuel.put("price", curFuel.getP2());
                fuel.put("durability", curFuel.getP3());
            }

            ItemProfile curShip = accountService.getItem(shipId);

            if (curShip != null) {
                ship.put("id", curShip.getId());
                ship.put("name", curShip.getName());
                ship.put("hp", curShip.getP1());
                ship.put("block", curShip.getP2());
                ship.put("price", curShip.getP3());
            }

            ItemProfile curEngine = accountService.getItem(engineId);

            if (curEngine != null) {
                engine.put("id", curEngine.getId());
                engine.put("name", curEngine.getName());
                engine.put("speed", curEngine.getP1());
                engine.put("giper", curEngine.getP2());
                engine.put("price", curEngine.getP3());
                engine.put("durability", curEngine.getP4());
            }

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
