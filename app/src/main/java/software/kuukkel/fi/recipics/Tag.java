package software.kuukkel.fi.recipics;

/**
 * Created by namiskuukkel on 7.6.2016.
 */
public class Tag {
    private String name;
    //Tag may have a color at some point
    private String color;
    public enum Type {
        DISH, INGREDIENT, ORIGIN, SWIFTNESS
    }
    private Type type;

    public Tag( String name) {
        this.name = name;
    }

    public Tag( String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Tag( String name, String color, Type type) {
        this.name = name;
        this.color = color;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
