package main;

import Users.AccountServiceImpl;
import base.AccountService;
import base.DBService;
import base.DbServiceImpl;
import dao.impl.PlayerDao;
import org.hibernate.Session;

/**
 * Created by Андрей on 21.09.2014.
 */
public class ConstructFactory {
    public AccountService accountService;
    private DBService dbService;

    public ConstructFactory() {}

    public ConstructFactory getInstance() {
        return  this;
    }

    public DBService getDBService() {
        dbService = new DbServiceImpl("hibernate.cfg.xml");
        return dbService;
    }

    public AccountService getAccountService(DBService dbService) {
        this.dbService = dbService;
        if(accountService == null) accountService = new AccountServiceImpl(this.dbService);
        return accountService;
    }
}
