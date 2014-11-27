package main;

import Users.AccountService;
import Users.UserProfile;
import admin.AdminPageServlet;
import base.WebSocketService;
import dao.PlayerDao;
import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import profiles.ItemProfile;
import profiles.SystemProfile;
import table.Player;
import timertasks.ScheduledTask;

import javax.servlet.Servlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;
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

        Factory factory = new Factory();
        PlayerDao playerDao = factory.getPlayerDao();
        List<Player> players = playerDao.getPlayers();

        AccountService accountService = new AccountService(playerDao);

        for(Player player : players) {
            UserProfile profile = new UserProfile(player);
            accountService.users.put(player.getLogin(), profile);
        }
        ////////////////////////////////////////////////////////////////
        Timer time = new Timer();
        ScheduledTask st = new ScheduledTask(accountService);
        time.schedule(st, 0, 60000);
        //////////////////////////////////////////
        File fXmlFile = new File("src/main/resources/systems.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("system");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            System.out.println("----------------------------\n");
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;
            System.out.println("id : " + eElement.getElementsByTagName("id").item(0).getTextContent());
            System.out.println("name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
            System.out.println("star : " + eElement.getElementsByTagName("star").item(0).getTextContent());
            System.out.println("background : " + eElement.getElementsByTagName("background").item(0).getTextContent());
            SystemProfile systemProfile = new SystemProfile(eElement.getElementsByTagName("id").item(0).getTextContent(),
                                                            eElement.getElementsByTagName("name").item(0).getTextContent(),
                                                            eElement.getElementsByTagName("star").item(0).getTextContent(),
                                                            eElement.getElementsByTagName("background").item(0).getTextContent());
            accountService.systems.put(eElement.getElementsByTagName("id").item(0).getTextContent(), systemProfile);
        }
        //-------------------------------------
        for(int i = 0; i < 8; i++) {
            String source = "ship";
            String[] mass = new String[6];
            mass[0] = "hp";
            mass[1] = "block";
            mass[2] = "price";
            mass[3] = null;
            mass[4] = null;
            mass[5] = null;
            if(i == 1) {
                source = "engine";
                mass[0] = "speed";
                mass[1] = "giper";
                mass[2] = "price";
                mass[3] = "durability";
            } else {
                if(i == 2) {
                    source = "gun";
                    mass[0] = "min";
                    mass[1] = "max";
                    mass[2] = "radius";
                    mass[3] = "recharge";
                    mass[4] = "price";
                    mass[5] = "durability";
                } else {
                    if(i == 3) {
                        source = "fuel";
                        mass[0] = "capacity";
                        mass[1] = "price";
                        mass[2] = "durability";
                    } else {
                        if(i == 4) {
                            source = "droid";
                            mass[0] = "repair";
                            mass[1] = "price";
                            mass[2] = "durability";
                        } else {
                            if(i == 5) {
                                source = "generator";
                                mass[0] = "block";
                                mass[1] = "price";
                                mass[2] = "durability";
                            } else {
                                if(i == 6) {
                                    source = "radar";
                                    mass[0] = "locate";
                                    mass[1] = "price";
                                    mass[2] = "durability";
                                } else {
                                    if(i == 7) {
                                        source = "scaner";
                                        mass[0] = "scan";
                                        mass[1] = "price";
                                        mass[2] = "durability";
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fXmlFile = new File("src/main/resources/items/"+source+".xml");
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();


            doc = db.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            nList = doc.getElementsByTagName(source);
            String[] mas = new String[6];

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                Element eElement = (Element) nNode;
                for(int j = 0; j < 6; j++) {
                    if(mass[j] != null) {
                        mas[j] = eElement.getElementsByTagName(mass[j]).item(0).getTextContent();
                    } else {
                        mas[j] = null;
                    }
                }
                ItemProfile itemProfile = new ItemProfile(eElement.getElementsByTagName("id").item(0).getTextContent(), i, eElement.getElementsByTagName("name").item(0).getTextContent(), mas[0], mas[1], mas[2], mas[3], mas[4], mas[5]);
                System.out.println("id : " + eElement.getElementsByTagName("id").item(0).getTextContent());
                System.out.println("name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
                accountService.items.put(eElement.getElementsByTagName("id").item(0).getTextContent(), itemProfile);
            }
        }
        ////////////////////////////////////////////////
        Servlet login = new LoginServlet(accountService);
        context.addServlet(new ServletHolder(login), "/api/v1/auth/signin");
        Servlet signUp = new SignUpServlet(accountService);
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        Servlet logout = new LogoutServlet(accountService);
        context.addServlet(new ServletHolder(logout), "/api/v1/auth/logout");
        Servlet admin = new AdminPageServlet(accountService);
        context.addServlet(new ServletHolder(admin), "/admin");

        Servlet score = new ScoreServlet();
        context.addServlet(new ServletHolder(score), "/api/v1/score");

        //Sockets
        WebSocketService webSocketService = new WebSocketService();
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