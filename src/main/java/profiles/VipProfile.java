package profiles;

/**
 * Created by surkov on 21.12.14.
 */
public class VipProfile {
    private int id;
    private String name;
    private String about;
    private int skill;
    private int count;
    private int price;

    public VipProfile(int id, String name, String about, int skill, int count, int price) {
        this.id = id;
        this.name = name;
        this.about = about;
        this.skill = skill;
        this.count = count;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public int getSkill() {
        return skill;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}
