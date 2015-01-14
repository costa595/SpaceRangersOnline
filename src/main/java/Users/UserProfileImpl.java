package Users;

import java.util.*;


import java.util.Date;

import table.Player;


/**
 * Created by dmitry on 13.09.14.
 */
public class UserProfileImpl implements UserProfile {
    private int id;
    private String email;
    private String password;
    private String login;
    private int avatar;
    private int race; // сделать enum
    /*
    0 - малок (red)
    1 - пеленг(green)
    2 - человек(blue)
    3 - фэянин(pink)
    4 - гаалец (yellow)
     */

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
    private int ship;
    private Map<String, Integer> inventory = new HashMap<>();
    private int skill0;
    private int skill1;
    private int skill2;
    private int skill3;
    private int skill4;
    private int skill5;
    private int score;
    private int killmobs;
    private int killplayers;
    private int exp;
    private int level;
    private int hp;

    public UserProfileImpl(Player player) {
        this.id = player.getId();
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
        this.ship = player.getShip();
        this.inventory.put("w1", player.getW1());
        this.inventory.put("w2", player.getW2());
        this.inventory.put("w3", player.getW3());
        this.inventory.put("w4", player.getW4());
        this.inventory.put("w5", player.getW5());
        this.inventory.put("engine", player.getEngine());
        this.inventory.put("fuel", player.getFuel());
        this.inventory.put("droid", player.getDroid());
        this.inventory.put("hook", player.getHook());
        this.inventory.put("generator", player.getGenerator());
        this.inventory.put("radar", player.getRadar());
        this.inventory.put("scaner", player.getScaner());
        this.inventory.put("hold0", player.getHold0());
        this.inventory.put("hold1", player.getHold1());
        this.inventory.put("hold2", player.getHold2());
        this.inventory.put("hold3", player.getHold3());
        this.inventory.put("hold4", player.getHold4());
        this.inventory.put("hold5", player.getHold5());
        this.skill0 = player.getSkill0();
        this.skill1 = player.getSkill1();
        this.skill2 = player.getSkill2();
        this.skill3 = player.getSkill3();
        this.skill4 = player.getSkill4();
        this.skill5 = player.getSkill5();
        this.score = player.getScore();
        this.killmobs = player.getKillmobs();
        this.killplayers = player.getKillplayers();
        this.exp = player.getExp();
        this.level = player.getLevel();
        this.hp = player.getHp();
        System.out.println("HP "+this.hp);
        this.prevX = 0;
        this.prevY = 0;
        this.lastActionTime = 0;
    }

    public UserProfileImpl(String login, String email, String password, int avatar, int race) {
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
        this.inventory.put("w1", 0);
        this.inventory.put("w2", 0);
        this.inventory.put("w3", 21);
        this.inventory.put("w4", 22);
        this.inventory.put("w5", 23);
        this.inventory.put("engine", 19);
        this.inventory.put("fuel", 29);
        this.inventory.put("radar", 45);
        this.ship = 1;
        this.level = 1;
        this.hp = 1000;
        this.prevX = 0;
        this.prevY = 0;
        this.lastActionTime = 0;
    }

    @Override
    public void updateCoords(float x, float y, float prevX, float prevY) {
        Date date = new Date();
        this.lastActionTime = date.getTime();
        this.prevX = prevX;
        this.prevY = prevY;
        this.x = x;
        this.y = y;
    }

    @Override
    public int updateHealth(float delthahealth) {
        this.hp += delthahealth;
        if (this.hp < 0){
            return 0;
        } else {
            return this.hp;
        }
    }

    @Override
    public void setEngine(int engine) {
        this.inventory.put("engine", engine);
    }
    @Override
    public void setFuel(int fuel) {
        this.inventory.put("fuel", fuel);
    }
    @Override
    public void setHook(int hook) {
        this.inventory.put("hook", hook);
    }
    @Override
    public void setW1(int w1) {
        this.inventory.put("w1", w1);
    }
    @Override
    public void setW2(int w2) {
        this.inventory.put("w2", w2);
    }
    @Override
    public void setW3(int w3) {
        this.inventory.put("w3", w3);
    }
    @Override
    public void setW4(int w4) {
        this.inventory.put("w4", w4);
    }
    @Override
    public void setW5(int w5) {
        this.inventory.put("w5", w5);
    }
    @Override
    public void setGenerator(int generator) {
        this.inventory.put("hold5", generator);
    }
    @Override
    public void setRadar(int radar) {
        this.inventory.put("hold5", radar);
    }
    @Override
    public void setScaner(int scaner) {
        this.inventory.put("hold5", scaner);
    }
    @Override
    public void setDroid(int droid) {
        this.inventory.put("hold5", droid);
    }
    @Override
    public void setLocation(int locationId){
        curLocationId = locationId;
    }
    @Override
    public void setHold0(int hold0) {
        this.inventory.put("hold0", hold0);
    }
    @Override
    public void setHold1(int hold1) {
        this.inventory.put("hold1", hold1);
    }
    @Override
    public void setHold2(int hold2) {
        this.inventory.put("hold2", hold2);
    }
    @Override
    public void setHold3(int hold3) {
        this.inventory.put("hold3", hold3);
    }
    @Override
    public void setHold4(int hold4) {
        this.inventory.put("hold4", hold4);
    }
    @Override
    public void setHold5(int hold5) {
        this.inventory.put("hold5", hold5);
    }

    @Override
    public int getId() { return id; }
    @Override
    public String getLogin() {
        return login;
    }
    @Override
    public String getEmail() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public int getAvatar() {
        return avatar;
    }
    @Override
    public int getRace() {return race;}
    @Override
    public int getAdmin() { return admin; }
    @Override
    public float getX(){ return x; }
    @Override
    public float getY(){ return y; }
    @Override
    public float getPrevX() { return prevX; }
    @Override
    public float getPrevY() { return prevY; }
    @Override
    public long getLastActionTime() { return lastActionTime; }
    @Override
    public int getLocationId(){ return curLocationId; }
    @Override
    public int getRank() {return rank;}
    @Override
    public int getCr() {return cr;}
    @Override
    public int getKr() {return kr;}
    @Override
    public int getW1() {return inventory.get("w1");}
    @Override
    public int getW2() {return inventory.get("w2");}
    @Override
    public int getW3() {return inventory.get("w3");}
    @Override
    public int getW4() {return inventory.get("w4");}
    @Override
    public int getW5() {return inventory.get("w5");}
    @Override
    public int getEngine() {return inventory.get("engine");}
    @Override
    public int getFuel() {return inventory.get("fuel");}
    @Override
    public int getDroid() {return inventory.get("droid");}
    @Override
    public int getHook() {return inventory.get("hook");}
    @Override
    public int getGenerator() {return inventory.get("generator");}
    @Override
    public int getRadar() {return inventory.get("radar");}
    @Override
    public int getScaner() {return inventory.get("scaner");}
    @Override
    public int getShip() {return ship;}

    @Override
    public int getSkill0() {
        return skill0;
    }

    @Override
    public int getSkill1() {
        return skill1;
    }

    @Override
    public int getSkill2() {
        return skill2;
    }

    @Override
    public int getSkill3() {
        return skill3;
    }

    @Override
    public int getSkill4() {
        return skill4;
    }

    @Override
    public int getSkill5() {
        return skill5;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getHold0() {
        return inventory.get("hold0");
    }

    @Override
    public int getHold1() {
        return inventory.get("hold1");
    }

    @Override
    public int getHold2() {
        return inventory.get("hold2");
    }

    @Override
    public int getHold3() {
        return inventory.get("hold3");
    }

    @Override
    public int getHold4() {
        return inventory.get("hold4");
    }

    @Override
    public int getHold5() {
        return inventory.get("hold5");
    }

    @Override
    public int getKillmobs() {
        return killmobs;
    }

    @Override
    public void setKillmobs(int killmobs) {
        this.killmobs = killmobs;
    }

    @Override
    public int getKillplayers() {
        return killplayers;
    }

    @Override
    public void setKillplayers(int killplayers) {
        this.killplayers = killplayers;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public Map<String, Integer> getInventory() {
        return inventory;
    }
    @Override
    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }
    @Override
    public int getHold(int i) {
        switch (i) {
            case 0:
                return getHold0();
            case 1:
                return getHold1();
            case 2:
                return getHold2();
            case 3:
                return getHold3();
            case 4:
                return getHold4();
            case 5:
                return getHold5();
        }
        return 0;
    }
    @Override
    public int getWeapon(int i) {
        switch (i) {
            case 1:
                return getW1();
            case 2:
                return getW2();
            case 3:
                return getW3();
            case 4:
                return getW4();
            case 5:
                return getW5();
        }
        return 0;
    }
}
