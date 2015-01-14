package frontend;

import base.AccountService;
import base.DBService;
import org.json.simple.JSONObject;
import table.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by surkov on 08.12.14.
 */
public class RatingServlet extends HttpServlet {

    public AccountService accountService;
    private DBService dbService;
    JSONObject output = new JSONObject();

    public RatingServlet(AccountService accountService, DBService dbService) {
        this.dbService = dbService;
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        List<Player> players = null;
        try {
            players = dbService.getPlayersRating();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int i = 1;
        JSONObject mainObj = new JSONObject();
        for(Player player : players) {
            JSONObject rateplayer = new JSONObject();
            rateplayer.put("login", player.getLogin());
            rateplayer.put("score", player.getScore());
            rateplayer.put("avatar", player.getAvatar());
            rateplayer.put("rank", player.getRank());
            rateplayer.put("race", player.getRace());
            rateplayer.put("killplayers", player.getKillplayers());
            rateplayer.put("killmobs", player.getKillmobs());
            mainObj.put(i, rateplayer);
            i++;
        }
        output.put("mainObj", mainObj);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json");
        response.getWriter().println(output);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
