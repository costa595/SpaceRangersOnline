package main;

import admin.AdminPageServlet;
import base.*;
import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import profiles.ProfileImportXml;
import timertasks.SaveDbTask;

import javax.servlet.Servlet;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8000;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        ConstructFactory factory = new ConstructFactory();

        DBService dbService = factory.getDBService();
        AccountService accountService = factory.getAccountService(dbService);

        Timer dbsave = new Timer();
        SaveDbTask st = new SaveDbTask(accountService);
        dbsave.schedule(st, 0, 60000); // поток для сохранения в БД раз в минуту

        ProfileImportXml profileImportXml = new ProfileImportXml(accountService);
        profileImportXml.importXml(); // Заполняем мапы данными из XML

        Servlet login = new LoginServlet(accountService);
        context.addServlet(new ServletHolder(login), "/api/v1/auth/signin");
        Servlet signUp = new SignUpServlet(accountService);
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        Servlet logout = new LogoutServlet(accountService);
        context.addServlet(new ServletHolder(logout), "/api/v1/auth/logout");
        Servlet market = new MarketServlet(accountService);
        context.addServlet(new ServletHolder(market), "/api/v1/market");
        Servlet rating = new RatingServlet(accountService, dbService);
        context.addServlet(new ServletHolder(rating), "/api/v1/rating");
        Servlet admin = new AdminPageServlet(accountService, dbService);
        context.addServlet(new ServletHolder(admin), "/admin");
        Servlet profiles = new ProfileServlet(accountService, dbService);
        context.addServlet(new ServletHolder(profiles), "/api/v1/profile");

        //Sockets
        WebSocketService webSocketService = new WebSocketServiceImpl();
        WebSocketGameServlet webSocketGameServlet = new WebSocketGameServlet(webSocketService, accountService);

        context.addServlet(new ServletHolder(webSocketGameServlet), "/api/v1/game");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}