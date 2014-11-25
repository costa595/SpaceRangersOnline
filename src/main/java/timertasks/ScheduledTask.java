package timertasks;

import Users.AccountService;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by пк on 18.11.2014.
 */
public class ScheduledTask extends TimerTask implements Runnable {
    AccountService accountService;
    public ScheduledTask(AccountService accountService) { this.accountService = accountService;}
    Date now;
    @Override
    public void run() {
        //accountService.saveToBd();
        System.out.println(accountService.countUsers());
        //now = new Date();
        //System.out.println("Текущая дата и время : " + now);
    }
}
