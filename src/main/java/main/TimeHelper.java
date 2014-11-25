package main;

/**
 * Created by Андрей on 21.09.2014.
 */
public class TimeHelper {
    public static void sleep(int period){
        try{
            Thread.sleep(period);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
