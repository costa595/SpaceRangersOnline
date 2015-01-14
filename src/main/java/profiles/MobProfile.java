package profiles;

/**
 * Created by surkov on 09.12.14.
 */
public class MobProfile {
    private int id;
    private String name;
    private int weapon0;
    private int weapon1;
    private int weapon2;
    private int weapon3;
    private int weapon4;
    private int block;
    private int blockbygen;
    private int speed;
    private int krit;
    private int manevr;
    private int drop0;
    private int drop1;
    private int drop2;
    private int drop3;
    private int drop4;
    private int drop5;

    public MobProfile(int id, String name, int weapon0, int weapon1, int weapon2, int weapon3, int weapon4, int block, int blockbygen,
                      int speed, int krit, int manevr, int drop0, int drop1, int drop2, int drop3, int drop4, int drop5) {
        this.id = id;
        this.name = name;
        this.weapon0 = weapon0;
        this.weapon1 = weapon1;
        this.weapon2 = weapon2;
        this.weapon3 = weapon3;
        this.weapon4 = weapon4;
        this.block = block;
        this.blockbygen = blockbygen;
        this.speed = speed;
        this.krit = krit;
        this.manevr = manevr;
        this.drop0 = drop0;
        this.drop1 = drop1;
        this.drop2 = drop2;
        this.drop3 = drop3;
        this.drop4 = drop4;
        this.drop5 = drop5;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getWeapon0() {
        return weapon0;
    }
    public int getWeapon1() {
        return weapon1;
    }
    public int getWeapon2() {
        return weapon2;
    }
    public int getWeapon3() {
        return weapon3;
    }
    public int getWeapon4() {
        return weapon4;
    }
    public int getBlock() {
        return block;
    }
    public int getBlockbygen() {
        return blockbygen;
    }
    public int getSpeed() {
        return speed;
    }
    public int getKrit() {
        return krit;
    }
    public int getManevr() {
        return manevr;
    }
    public int getDrop0() {
        return drop0;
    }
    public int getDrop1() {
        return drop1;
    }
    public int getDrop2() {
        return drop2;
    }
    public int getDrop3() {
        return drop3;
    }
    public int getDrop4() {
        return drop4;
    }
    public int getDrop5() {
        return drop5;
    }
}
