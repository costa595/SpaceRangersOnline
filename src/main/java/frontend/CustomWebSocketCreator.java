package frontend;

import Users.AccountService;
import base.WebSocketService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * Created by Андрей on 19.11.2014.
 */
public class CustomWebSocketCreator implements WebSocketCreator {
    private WebSocketService webSocketService;
    private AccountService accountService;

    public CustomWebSocketCreator(WebSocketService webSocketService,
                                  AccountService accountService) {
        this.webSocketService = webSocketService;
        this.accountService = accountService;
    }

    //Создание нового сокета для каждого пользака
    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        final String sessionId = request.getHttpServletRequest().getSession().getId();
        System.out.println("new GameWebSocket " + sessionId);
        return new GameWebSocket(sessionId, webSocketService, accountService);

    }
}
