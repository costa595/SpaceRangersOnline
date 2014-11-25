package frontend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by пк on 03.10.2014.
 */
public class ScoreServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        JSONArray scores = new JSONArray();
        JSONObject score = new JSONObject();
        score.put("login", "root");
        score.put("value", "0");
        scores.add(score);
        JSONObject score2 = new JSONObject();
        score2.put("login", "root");
        score2.put("value", "0");
        scores.add(score2);
        JSONObject score3 = new JSONObject();
        score3.put("login", "root");
        score3.put("value", "0");
        scores.add(score3);
        JSONObject score4 = new JSONObject();
        score4.put("login", "root");
        score4.put("value", "0");
        scores.add(score4);
        response.setHeader("Content-type", "application/json");
        response.getWriter().println(scores);
    }
}
