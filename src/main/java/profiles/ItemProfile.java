package profiles;

/**
 * Created by Андрей on 24.11.2014.
 */
public class ItemProfile {
    private int id;
    private int type;
    private String name;
    private String price;
    private String p1;
    private String p2;
    private String p3;
    private String p4;
    private String p5;

    public ItemProfile(int id, int type, String name, String price, String p1, String p2, String p3, String p4, String p5) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
    }

    public int getId() {
        return id;
    }
    public int getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    public String getP1() {
        return p1;
    }
    public String getP2() {
        return p2;
    }
    public String getP3() {
        return p3;
    }
    public String getP4() {
        return p4;
    }
    public String getP5() {
        return p5;
    }
    public String getPrice() {
        return price;
    }
}
