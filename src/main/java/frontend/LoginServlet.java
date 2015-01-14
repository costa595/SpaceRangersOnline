package frontend;

import Users.UserProfile;
import base.AccountService;
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
import java.sql.SQLException;

/**
 * Created by dmitry on 14.09.14.
 */
public class LoginServlet extends HttpServlet {

    private AccountService accountService;
    private JSONObject output = new JSONObject();
    private JSONObject system = new JSONObject();
    private JSONObject engine = new JSONObject();
    private JSONObject ship = new JSONObject();
    private JSONObject fuel = new JSONObject();
    private JSONObject droid = new JSONObject();
    private JSONObject radar = new JSONObject();
    private JSONObject scaner = new JSONObject();
    private JSONObject generator = new JSONObject();
    private JSONObject hook = new JSONObject();

    public LoginServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        UserProfile currentUser = accountService.getCurrentUser(sessionId);
        System.out.println("doGet "+sessionId+" "+currentUser);

        if (currentUser != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            output.put("sessionId", sessionId);
            output = makeUserData(currentUser, output);

            response.setHeader("Content-type", "application/json");
            response.setCharacterEncoding("UTF-8");
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
        HttpSession session = request.getSession();

        System.out.println("doPost");
        try {
            if (accountService.login(login, password, session.getId()) == CodeResponses.OK) {
                String sessionId = session.getId();
                UserProfile currentProfile = accountService.getCurrentUser(session.getId());
                output.put("status", 200);
                output.put("login", currentProfile.getLogin());
                output.put("email", currentProfile.getEmail());
                output.put("avatar", currentProfile.getAvatar());
                output.put("sessionId", sessionId);
                output = makeUserData(currentProfile, output);
            }
            else {
                output.put("status", 403);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-type", "application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(output);
    }

    private JSONObject makeUserData(UserProfile currentUser, JSONObject output) {

        int sysId = 0;
        int engineId = 0;
        int shipId = 0;
        int fuelId = 0;
        int droidId = 0;
        int radarId = 0;
        int scanerId = 0;
        int hookId = 0;
        int generatorId = 0;
        boolean anyErrors = false;
        try {
            sysId = currentUser.getLocationId();
            engineId = currentUser.getEngine();
            shipId = currentUser.getShip();
            fuelId = currentUser.getFuel();

            droidId = currentUser.getDroid();
            radarId = currentUser.getRadar();
            scanerId = currentUser.getScaner();
            generatorId = currentUser.getGenerator();
            hookId = currentUser.getHook();


        } catch (Exception e) {
            anyErrors = true;
        }
        if (!anyErrors){
            SystemProfile currentSystem = accountService.getCurrentSystem(sysId);

            String type = null;
            String[] mass = new String[6];
            for(int i = 0; i < 6; i++) {
                JSONObject hold = new JSONObject();
                ItemProfile curHold = accountService.getItem(currentUser.getHold(i));
                if(curHold != null) {
                    switch (curHold.getType()) {
                        case 0:
                            type = "ship";
                            mass[0] = "hp";
                            mass[1] = "block";
                            break;
                        case 1:
                            type = "engine";
                            mass[0] = "speed";
                            mass[1] = "giper";
                            mass[2] = "durability";
                            break;
                        case 2:
                            type = "gun";
                            mass[0] = "min";
                            mass[1] = "max";
                            mass[2] = "radius";
                            mass[3] = "recharge";
                            mass[4] = "durability";
                            break;
                        case 3:
                            type = "fuel";
                            mass[0] = "capacity";
                            mass[1] = "durability";
                            break;
                        case 4:
                            type = "droid";
                            mass[0] = "repair";
                            mass[1] = "durability";
                            break;
                        case 5:
                            type = "generator";
                            mass[0] = "block";
                            mass[1] = "durability";
                            break;
                        case 6:
                            type = "radar";
                            mass[0] = "locate";
                            mass[1] = "durability";
                            break;
                        case 7:
                            type = "scaner";
                            mass[0] = "scan";
                            mass[1] = "durability";
                            break;

                    }
                    hold.put("id", curHold.getId());
                    hold.put("name", curHold.getName());
                    hold.put("price", curHold.getPrice());
                    hold.put("type", type);
                    hold.put(mass[0], curHold.getP1());
                    hold.put(mass[1], curHold.getP2());
                    hold.put(mass[2], curHold.getP3());
                    hold.put(mass[3], curHold.getP4());
                    hold.put(mass[4], curHold.getP5());
                    for(int j = 0; j < 5; j++) mass[j] = null;
                } else {
                    hold.put("id", 0);
                }
                output.put("hold"+i, hold);
            }

            for(int i = 1; i < 6; i++) {
                JSONObject gun = new JSONObject();
                ItemProfile curGun = accountService.getItem(currentUser.getWeapon(i));
                if (curGun != null) {
                    gun.put("id", curGun.getId());
                    gun.put("name", curGun.getName());
                    gun.put("min", curGun.getP1());
                    gun.put("max", curGun.getP2());
                    gun.put("radius", curGun.getP3());
                    gun.put("recharge", curGun.getP4());
                    gun.put("price", curGun.getPrice());
                    gun.put("durability", curGun.getP5());
                } else {
                    gun.put("id", 0);
                }
                output.put("w"+i, gun);
            }

            ItemProfile curGenerator = accountService.getItem(generatorId);

            if (curGenerator != null) {
                generator.put("id", curGenerator.getId());
                generator.put("name", curGenerator.getName());
                generator.put("block", curGenerator.getP1());
                generator.put("price", curGenerator.getPrice());
                generator.put("durability", curGenerator.getP2());
            } else {
                generator.put("id", 0);
            }

            ItemProfile curHook = accountService.getItem(hookId);

            if (curHook != null) { // пока этот предмет отсутствует в игре
                hook.put("id", curHook.getId());
                hook.put("name", curHook.getName());
                hook.put("block", curHook.getP1());
                hook.put("price", curHook.getPrice());
                hook.put("durability", curHook.getP2());
            } else {
                hook.put("id", 0);
            }

            ItemProfile curDroid = accountService.getItem(droidId);

            if (curDroid != null) {
                droid.put("id", curDroid.getId());
                droid.put("name", curDroid.getName());
                droid.put("repair", curDroid.getP1());
                droid.put("price", curDroid.getPrice());
                droid.put("durability", curDroid.getP2());
            } else {
                droid.put("id", 0);
            }

            ItemProfile curScaner = accountService.getItem(scanerId);

            if (curScaner != null) {
                scaner.put("id", curScaner.getId());
                scaner.put("name", curScaner.getName());
                scaner.put("scan", curScaner.getP1());
                scaner.put("price", curScaner.getPrice());
                scaner.put("durability", curScaner.getP2());
            } else {
                scaner.put("id", 0);
            }


            ItemProfile curRadar = accountService.getItem(radarId);

            if (curRadar != null) {
                radar.put("id", curRadar.getId());
                radar.put("name", curRadar.getName());
                radar.put("locate", curRadar.getP1());
                radar.put("price", curRadar.getPrice());
                radar.put("durability", curRadar.getP2());
            } else {
                radar.put("id", 0);
            }


            ItemProfile curFuel = accountService.getItem(fuelId);

            if (curFuel != null) {
                fuel.put("id", curFuel.getId());
                fuel.put("name", curFuel.getName());
                fuel.put("capacity", curFuel.getP1());
                fuel.put("price", curFuel.getPrice());
                fuel.put("durability", curFuel.getP2());
            } else {
                fuel.put("id", 0);
            }

            ItemProfile curShip = accountService.getItem(shipId);

            if (curShip != null) {
                ship.put("id", curShip.getId());
                ship.put("name", curShip.getName());
                ship.put("hp", curShip.getP1());
                ship.put("block", curShip.getP2());
                ship.put("price", curShip.getPrice());
            } else {
                ship.put("id", 0);
            }

            ItemProfile curEngine = accountService.getItem(engineId);

            if (curEngine != null) {
                engine.put("id", curEngine.getId());
                engine.put("name", curEngine.getName());
                engine.put("speed", curEngine.getP1());
                engine.put("giper", curEngine.getP2());
                engine.put("price", curEngine.getPrice());
                engine.put("durability", curEngine.getP3());
            } else {
                engine.put("id", 0);
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

            output.put("race", currentUser.getRace());
            output.put("x", currentUser.getX());
            output.put("y", currentUser.getY());
            output.put("rank", currentUser.getRank());
            output.put("cr", currentUser.getCr());
            output.put("kr", currentUser.getKr());
            output.put("droid", droid);
            output.put("hook", hook);
            output.put("generator", generator);
            output.put("radar", radar);
            output.put("scanner", scaner);
            output.put("level", currentUser.getLevel());

        }

        return output;
    }
}
