package timertasks;

import base.AccountService;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by пк on 18.11.2014.
 */
public class SaveDbTask extends TimerTask implements Runnable {
    AccountService accountService;
    public SaveDbTask(AccountService accountService) { this.accountService = accountService;}
    Date now;
    @Override
    public void run() {
        //accountService.saveToBd();
        System.out.println(accountService.countUsers());
        //now = new Date();
        //System.out.println("Текущая дата и время : " + now);
    }
}
