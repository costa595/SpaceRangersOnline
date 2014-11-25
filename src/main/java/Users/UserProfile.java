package Users;

import java.util.Date;


import java.util.Date;

import table.Player;


/**
 * Created by dmitry on 13.09.14.
 */
public class UserProfile {
    private String email;
    private String password;
    private String login;
    private int avatar;
    private int race;
    private int admin;
    private float x;
    private float y;

    private float prevX;
    private float prevY;
    private long lastActionTime;

    private int curLocationId;
    private int rank;
    private int cr;
    private int kr;
    private int w1;
    private int w2;
    private int w3;
    private int w4;
    private int w5;
    private int engine;
    private int fuel;
    private int droid;
    private int hook;
    private int generator;
    private int radar;
    private int scaner;
    private int ship;


    public UserProfile(Player player) {

        this.email = player.getMail();
        this.password = player.getPass();
        this.login = player.getLogin();
        this.avatar = player.getAvatar();
        this.race = player.getRace();
        this.admin = player.getAdmin();
        this.curLocationId = player.getSystem();
        this.x = player.getX();
        this.y = player.getY();
        this.rank = player.getRank();
        this.cr = player.getCr();
        this.kr = player.getKr();
        this.w1 = player.getW1();
        this.w2 = player.getW2();
        this.w3 = player.getW3();
        this.w4 = player.getW4();
        this.w5 = player.getW5();
        this.engine = player.getEngine();
        this.fuel = player.getFuel();
        this.droid = player.getDroid();
        this.hook = player.getHook();
        this.generator = player.getGenerator();
        this.radar = player.getRadar();
        this.scaner = player.getScaner();
        this.ship = player.getShip();

        this.prevX = 0;
        this.prevY = 0;
        this.lastActionTime = 0;
    }

    public UserProfile(String login, String email, String password, int avatar, int race) {
        this.email = email;
        this.password = password;
        this.login = login;

        this.admin = admin;
        this.curLocationId = 1; //Пока что костыль на одну локацию. Позже при перелете между локациями убрать это
        this.prevX = 0;
        this.prevY = 0;

        this.avatar = avatar;
        this.race = race;
        this.curLocationId = 0;
        this.x = 0;
        this.y = 0;
        this.rank = 0;
        this.cr = 0;
        this.kr = 0;
        this.w1 = 0;
        this.w2 = 0;
        this.w3 = 0;
        this.w4 = 0;
        this.w5 = 0;
        this.engine = 0;
        this.fuel = 0;
        this.droid = 0;
        this.hook = 0;
        this.generator = 0;
        this.radar = 0;
        this.scaner = 0;
        this.ship = 0;

        this.prevX = 0;
        this.prevY = 0;
        this.lastActionTime = 0;
    }

    public void updateCoords(float x, float y) {
        Date date = new Date();
        this.lastActionTime = date.getTime();
        this.prevX = this.x;
        this.prevY = this.y;
        this.x = x;
        this.y = y;
    }

    public void setLocation(int locationId){
        curLocationId = locationId;
    }

    public String getLogin() {
        return login;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public int getAvatar() {
        return avatar;
    }
    public int getRace() {return race;}
    public int getAdmin() { return admin; }
    public float getX(){ return x; }
    public float getY(){ return y; }
    public float getPrevX() { return prevX; }
    public float getPrevY() { return prevY; }
    public long getLastActionTime() { return lastActionTime; }
    public int getLocationId(){ return curLocationId; }
    public int getRank() {return rank;}
    public int getCr() {return cr;}
    public int getKr() {return kr;}
    public int getW1() {return w1;}
    public int getW2() {return w2;}
    public int getW3() {return w3;}
    public int getW4() {return w4;}
    public int getW5() {return w5;}
    public int getEngine() {return engine;}
    public int getFuel() {return fuel;}
    public int getDroid() {return droid;}
    public int getHook() {return hook;}
    public int getGenerator() {return generator;}
    public int getRadar() {return radar;}
    public int getScaner() {return scaner;}
    public int getShip() {return ship;}
}
