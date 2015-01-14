package frontend;

import base.AccountService;
import base.WebSocketService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Андрей on 19.11.2014.
 */
@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/game"})
public class WebSocketGameServlet extends WebSocketServlet {
    //    public final static String gamePageURL = "/api/v1/push";
    private final static int IDLE_TIME = 60 * 1000;
    private WebSocketService webSocketService;
    private AccountService accountService;

    public WebSocketGameServlet(WebSocketService webSocketService,
                                AccountService accountService) {
        this.webSocketService = webSocketService;
        this.accountService = accountService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new CustomWebSocketCreator(webSocketService, accountService));
    }
}
