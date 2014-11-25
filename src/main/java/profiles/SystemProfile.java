package profiles;

/**
 * Created by surkov on 24.11.14.
 */
public class SystemProfile {
    private String id;
    private String name;
    private String star;
    private String background;
//    private String planetId1;
//    private String planetName1;
//    private String planetRace1;
//    private String planetSprite1;
//    private String planetTexture1;
//    private String planetX1;
//    private String planetSpeed1;
//    private String planetCource1;


    public SystemProfile(String id, String name, String star, String background) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.background = background;
//        this.planetId1 = planetId1;
//        this.planetName1 = planetName1;
//        this.planetRace1 = planetRace1;

    }

    public String getId() {return id;}
    public String getName() {return name;}
    public String getStar() {return star;}
    public String getBackground() {return background;}
//    public String getPlanetId1() {return planetId1;}
//    public String getPlanetName1() {return planetName1;}
//    public String getPlanetRace1() {return planetRace1;}
//    public String getPlanetSprite1() { return planetSprite1; }
//    public String getPlanetTexture1() {
//        return planetTexture1;
//    }
//    public String getPlanetX1() {
//        return planetX1;
//    }
//    public String getPlanetSpeed1() {
//        return planetSpeed1;
//    }
//    public String getPlanetCource1() {
//        return planetCource1;
//    }
}
