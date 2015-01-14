package Users;

import java.util.Map;

/**
 * Created by surkov on 21.12.14.
 */
public interface UserProfile {
    void updateCoords(float x, float y, float prevX, float prevY);

    int updateHealth(float delthahealth);

    void setEngine(int engine);

    void setFuel(int fuel);

    void setHook(int hook);

    void setW1(int w1);

    void setW2(int w2);

    void setW3(int w3);

    void setW4(int w4);

    void setW5(int w5);

    void setGenerator(int generator);

    void setRadar(int radar);

    void setScaner(int scaner);

    void setDroid(int droid);

    void setLocation(int locationId);

    void setHold0(int hold0);

    void setHold1(int hold1);

    void setHold2(int hold2);

    void setHold3(int hold3);

    void setHold4(int hold4);

    void setHold5(int hold5);

    int getId();

    String getLogin();

    String getEmail();

    String getPassword();

    int getAvatar();

    int getRace();

    int getAdmin();

    float getX();

    float getY();

    float getPrevX();

    float getPrevY();

    long getLastActionTime();

    int getLocationId();

    int getRank();

    int getCr();

    int getKr();

    int getW1();

    int getW2();

    int getW3();

    int getW4();

    int getW5();

    int getEngine();

    int getFuel();

    int getDroid();

    int getHook();

    int getGenerator();

    int getRadar();

    int getScaner();

    int getShip();

    int getSkill0();

    int getSkill1();

    int getSkill2();

    int getSkill3();

    int getSkill4();

    int getSkill5();

    int getScore();

    int getHold0();

    int getHold1();

    int getHold2();

    int getHold3();

    int getHold4();

    int getHold5();

    int getKillmobs();

    void setKillmobs(int killmobs);

    int getKillplayers();

    void setKillplayers(int killplayers);

    int getExp();

    void setExp(int exp);

    int getLevel();

    void setLevel(int level);

    int getHp();

    void setHp(int hp);

    Map<String, Integer> getInventory();

    void setInventory(Map<String, Integer> inventory);

    int getHold(int i);

    int getWeapon(int i);
}
