package table;

import Users.UserProfile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Андрей on 21.09.2014.
 */
@Entity
@Table(name="players")
public class Player {
    @Id
    @Column(name="id")
    private int id;

    @Column(name="login")
    private String login;

    @Column(name="pass")
    private String pass;

    @Column(name="mail")
    private String mail;

    @Column(name="avatar")
    private int avatar;

    @Column(name="race")
    private int race;

    @Column(name="admin")
    private int admin;

    @Column(name="x")
    private float x;

    @Column(name="y")
    private float y;

    @Column(name="system")
    private int system;

    @Column(name="rank")
    private int rank;

    @Column(name="cr")
    private int cr;

    @Column(name="kr")
    private int kr;

    @Column(name="w1")
    private int w1;
    @Column(name="w2")
    private int w2;
    @Column(name="w3")
    private int w3;
    @Column(name="w4")
    private int w4;
    @Column(name="w5")
    private int w5;

    @Column(name="engine")
    private int engine;

    @Column(name="fuel")
    private int fuel;

    @Column(name="droid")
    private int droid;

    @Column(name="hook")
    private int hook;

    @Column(name="generator")
    private int generator;

    @Column(name="radar")
    private int radar;

    @Column(name="scaner")
    private int scaner;

    @Column(name="ship")
    private int ship;

    @Column(name="skill0")
    private int skill0;

    @Column(name="skill1")
    private int skill1;

    @Column(name="skill2")
    private int skill2;

    @Column(name="skill3")
    private int skill3;

    @Column(name="skill4")
    private int skill4;

    @Column(name="skill5")
    private int skill5;

    @Column(name="score")
    private int score;

    @Column(name="hold0")
    private int hold0;

    @Column(name="hold1")
    private int hold1;

    @Column(name="hold2")
    private int hold2;

    @Column(name="hold3")
    private int hold3;

    @Column(name="hold4")
    private int hold4;

    @Column(name="hold5")
    private int hold5;

    @Column(name="killmobs")
    private int killmobs;

    @Column(name="killplayers")
    private int killplayers;

    @Column(name="exp")
    private int exp;

    @Column(name="level")
    private int level;

    @Column(name="hp")
    private int hp;

    public Player() {}

    public Player(UserProfile userProfile) {
        this.id = userProfile.getId();
        this.login = userProfile.getLogin();
        this.pass = userProfile.getPassword();
        this.mail = userProfile.getEmail();
        this.avatar = userProfile.getAvatar();
        this.race = userProfile.getRace();
        this.admin = userProfile.getAdmin();
        this.x = userProfile.getX();
        this.y = userProfile.getY();
        this.system = userProfile.getLocationId();
        this.rank = userProfile.getRank();
        this.cr = userProfile.getCr();
        this.kr = userProfile.getKr();
        this.w1 = userProfile.getW1();
        this.w2 = userProfile.getW2();
        this.w3 = userProfile.getW3();
        this.w4 = userProfile.getW4();
        this.w5 = userProfile.getW5();
        this.engine = userProfile.getEngine();
        this.fuel = userProfile.getFuel();
        this.droid = userProfile.getDroid();
        this.hook = userProfile.getHook();
        this.generator = userProfile.getGenerator();
        this.radar = userProfile.getRadar();
        this.scaner = userProfile.getScaner();
        this.ship = userProfile.getShip();
        this.skill0 = userProfile.getSkill0();
        this.skill1 = userProfile.getSkill1();
        this.skill2 = userProfile.getSkill2();
        this.skill3 = userProfile.getSkill3();
        this.skill4 = userProfile.getSkill4();
        this.skill5 = userProfile.getSkill5();
        this.score = userProfile.getScore();
        this.hold0 = userProfile.getHold0();
        this.hold1 = userProfile.getHold1();
        this.hold2 = userProfile.getHold2();
        this.hold3 = userProfile.getHold3();
        this.hold4 = userProfile.getHold4();
        this.hold5 = userProfile.getHold5();
        this.hp = userProfile.getHp();

    }

    public void setId(int id) {
        this.id = id;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setAvatar(int avatar) { this.avatar = avatar; }
    public void setRace(int race) { this.race = race; }
    public void setAdmin(int admin) { this.admin = admin; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setSystem(int system) { this.system = system; }
    public void setRank(int rank) { this.rank = rank; }
    public void setCr(int cr) { this.cr = cr; }
    public void setKr(int kr) { this.kr = kr; }
    public void setW1(int w1) { this.w1 = w1; }
    public void setW2(int w2) { this.w2 = w2; }
    public void setW3(int w3) { this.w3 = w3; }
    public void setW4(int w4) { this.w4 = w4; }
    public void setW5(int w5) { this.w5 = w5; }
    public void setEngine(int engine) { this.engine = engine; }
    public void setFuel(int fuel) { this.fuel = fuel; }
    public void setDroid(int droid) {this.droid = droid;}
    public void setHook(int hook) {this.hook = hook;}
    public void setGenerator(int generator) {this.generator = generator;}
    public void setRadar(int radar) {this.radar = radar;}
    public void setScaner(int scaner) {this.scaner = scaner;}

    public int getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getPass() {
        return pass;
    }
    public String getMail() {
        return mail;
    }
    public int getAvatar() { return avatar; }
    public int getRace() {return race;}
    public int getAdmin() {return admin;}
    public float getX() {return x;}
    public float getY() {return y;}
    public int getSystem() {return system;}
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

    public int getShip() {
        return ship;
    }

    public void setShip(int ship) {
        this.ship = ship;
    }

    public int getSkill0() {
        return skill0;
    }

    public void setSkill0(int skill0) {
        this.skill0 = skill0;
    }

    public int getSkill1() {
        return skill1;
    }

    public void setSkill1(int skill1) {
        this.skill1 = skill1;
    }

    public int getSkill2() {
        return skill2;
    }

    public void setSkill2(int skill2) {
        this.skill2 = skill2;
    }

    public int getSkill3() {
        return skill3;
    }

    public void setSkill3(int skill3) {
        this.skill3 = skill3;
    }

    public int getSkill4() {
        return skill4;
    }

    public void setSkill4(int skill4) {
        this.skill4 = skill4;
    }

    public int getSkill5() {
        return skill5;
    }

    public void setSkill5(int skill5) {
        this.skill5 = skill5;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHold0() {
        return hold0;
    }

    public void setHold0(int hold0) {
        this.hold0 = hold0;
    }

    public int getHold1() {
        return hold1;
    }

    public void setHold1(int hold1) {
        this.hold1 = hold1;
    }

    public int getHold2() {
        return hold2;
    }

    public void setHold2(int hold2) {
        this.hold2 = hold2;
    }

    public int getHold3() {
        return hold3;
    }

    public void setHold3(int hold3) {
        this.hold3 = hold3;
    }

    public int getHold4() {
        return hold4;
    }

    public void setHold4(int hold4) {
        this.hold4 = hold4;
    }

    public int getHold5() {
        return hold5;
    }

    public void setHold5(int hold5) {
        this.hold5 = hold5;
    }

    public int getKillmobs() {
        return killmobs;
    }

    public void setKillmobs(int killmobs) {
        this.killmobs = killmobs;
    }

    public int getKillplayers() {
        return killplayers;
    }

    public void setKillplayers(int killplayers) {
        this.killplayers = killplayers;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setInventory(UserProfile userProfile) {
        this.w1 = userProfile.getInventory().get("w1");
        this.w2 = userProfile.getInventory().get("w2");
        this.w3 = userProfile.getInventory().get("w3");
        this.w4 = userProfile.getInventory().get("w4");
        this.w5 = userProfile.getInventory().get("w5");
        this.engine = userProfile.getInventory().get("engine");
        this.hook = userProfile.getInventory().get("hook");
        this.fuel = userProfile.getInventory().get("fuel");
        this.droid = userProfile.getInventory().get("droid");
        this.generator = userProfile.getInventory().get("generator");
        this.radar = userProfile.getInventory().get("radar");
        this.scaner = userProfile.getInventory().get("scaner");
        this.hold0 = userProfile.getInventory().get("hold0");
        this.hold1 = userProfile.getInventory().get("hold1");
        this.hold2 = userProfile.getInventory().get("hold2");
        this.hold3 = userProfile.getInventory().get("hold3");
        this.hold4 = userProfile.getInventory().get("hold4");
        this.hold5 = userProfile.getInventory().get("hold5");
    }

    public void setHp(int hp) { this.hp = hp; }
    public int getHp() { return hp; }
}
