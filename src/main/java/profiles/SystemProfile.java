package profiles;

/**
 * Created by surkov on 24.11.14.
 */
public class SystemProfile {
    private String id;
    private String name;
    private String star;
    private String background;

    public SystemProfile(String id, String name, String star, String background) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.background = background;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public String getStar() {return star;}
    public String getBackground() {return background;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemProfile that = (SystemProfile) o;

        if (background != null ? !background.equals(that.background) : that.background != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (star != null ? !star.equals(that.star) : that.star != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (star != null ? star.hashCode() : 0);
        result = 31 * result + (background != null ? background.hashCode() : 0);
        return result;
    }
}
