package main;

import dao.PlayerDao;
import dao.impl.PlayerDaoImpl;

/**
 * Created by Андрей on 21.09.2014.
 */
public class Factory {
    /*
    public static Factory instance = new Factory(); // при многопоточности эту шнягу надо будет как-то синхронизировать :((
    public PlayerDao playerDao;

    private Factory() { }

    public static Factory getInstance() {
        return  Factory.instance;
    }
    public PlayerDao getPlayerDao() {
        if (playerDao == null)  playerDao = new PlayerDaoImpl();
        return playerDao;
    }
    */
//    public Factory instance; // при многопоточности эту шнягу надо будет как-то синхронизировать :((
    public PlayerDao playerDao;

    //private Factory() { }
    public Factory() {
//        public Factory instance = new Factory();
    }

    public Factory getInstance() {
        return  this;
    }
    public PlayerDao getPlayerDao() {
        if (playerDao == null)  playerDao = new PlayerDaoImpl();
        return playerDao;
    }
}
