package software.kuukkel.fi.recipics.Objects;

import android.graphics.Color;

/**
 * Created by namiskuukkel on 7.6.2016.
 */
public class Tag {
    private com.cunoraz.tagview.Tag tag;
    String color;
    //Todo: Add type functionality (same type tags get grouped etc)
    public enum Type {
        DISH, INGREDIENT, ORIGIN, SWIFTNESS, UNDEFINED
    }
    private Type type;
    //Eg. Pork is Meat
    private String parentCategory = "";

    public Tag(String name) {
        tag = new com.cunoraz.tagview.Tag(name);
        tag.isDeletable = false;
    }
    //Todo: This is on temporary to test the recipe saving with db
    public Tag(com.cunoraz.tagview.Tag tag) {
        this.tag = tag;
    }

    public Tag(String name, String color) {
        tag = new com.cunoraz.tagview.Tag(name);
        tag.isDeletable = false;
        this.color = color;
        tag.layoutColor = Color.parseColor(color);
    }

    public Tag(String name, String color, Type type, String parentCategory) {
        tag = new com.cunoraz.tagview.Tag(name);
        tag.isDeletable = false;
        this.color = color;
        tag.layoutColor = Color.parseColor(color);
        this.type = type;
        this.parentCategory = parentCategory;
    }

    public String getName() {
        return tag.text;
    }

    public void setName(String name) {
        tag.text = name;
    }

    public String getColor() { return this.color; }

    public com.cunoraz.tagview.Tag getTag() { return tag; }

    public void setTag(com.cunoraz.tagview.Tag tag ) { this.tag = tag;}

    public Type getType() { return this.type; }

    public void setType(Type type) {
        this.type = type;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }
}
