package table;

//import org.hibernate.annotations.Entity;
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
}
