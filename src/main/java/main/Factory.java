package main;

import base.PlayerDao;
import dao.impl.PlayerDaoImpl;

/**
 * Created by Андрей on 21.09.2014.
 */
public class Factory {
    public PlayerDao playerDao;

    public Factory() {}

    public Factory getInstance() {
        return  this;
    }
    public PlayerDao getPlayerDao() {
        if (playerDao == null)  playerDao = new PlayerDaoImpl();
        return playerDao;
    }
}
