package frontend;

import base.AccountService;
import org.json.simple.JSONObject;
import profiles.ItemProfile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by surkov on 27.11.14.
 */
public class MarketServlet extends HttpServlet {
    public AccountService accountService;

    public MarketServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject goods = new JSONObject();


        int itemsCount = accountService.countItems();
        int itemType;
        int j;
        String id;
        for(int i = 0; i < itemsCount; i++) {
            JSONObject good = new JSONObject();
            j = i + 1;
            id = new Integer(j).toString();
            ItemProfile curItem = accountService.getItems().get(id);
            itemType = curItem.getType();
            switch(itemType) {
                case 0:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("hp", curItem.getP1());
                    good.put("block", curItem.getP2());
                    good.put("price", curItem.getPrice());
                    goods.put("good" + i, good);
                    break;
                case 1:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("speed", curItem.getP1());
                    good.put("giper", curItem.getP2());
                    good.put("price", curItem.getPrice());
                    good.put("durability", curItem.getP3());
                    goods.put("good" + i, good);
                    break;
                case 2:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("min", curItem.getP1());
                    good.put("max", curItem.getP2());
                    good.put("radius", curItem.getP3());
                    good.put("recharge", curItem.getP4());
                    good.put("price", curItem.getPrice());
                    good.put("durability", curItem.getP5());
                    goods.put("good" + i, good);
                    break;
                case 3:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("capacity", curItem.getP1());
                    good.put("price", curItem.getPrice());
                    good.put("durability", curItem.getP2());
                    goods.put("good" + i, good);
                    break;
                case 4:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("repair", curItem.getP1());
                    good.put("price", curItem.getPrice());
                    good.put("durability", curItem.getP2());
                    goods.put("good" + i, good);
                    break;
                case 5:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("block", curItem.getP1());
                    good.put("price", curItem.getPrice());
                    good.put("durability", curItem.getP2());
                    goods.put("good" + i, good);
                    break;
                case 6:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("locate", curItem.getP1());
                    good.put("price", curItem.getPrice());
                    good.put("durability", curItem.getP2());
                    goods.put("good" + i, good);
                    break;
                case 7:
                    good.put("id", curItem.getId());
                    good.put("name", curItem.getName());
                    good.put("scan", curItem.getP1());
                    good.put("price", curItem.getPrice());
                    good.put("durability", curItem.getP2());
                    goods.put("good" + i, good);
                    break;

            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json");
        response.getWriter().println(goods);
    }
}
