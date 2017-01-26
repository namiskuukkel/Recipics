package software.kuukkel.fi.recipics.Objects;

import java.util.ArrayList;

/**
 * Created by namiskuukkel on 6.6.2016.
 */
public class Recipe {

    private int id;
    private String name = "";
    //Filepaths of the pictures connected to the recipe
    private ArrayList<String> picturePaths;
    private String notes = "";
    private Boolean starred = false;
    private String source = "";
    private ArrayList<Tag> tags;

    public Recipe() {
        picturePaths = new ArrayList<>();
    }

    public Recipe( int id, String name, ArrayList<String> picturePaths, String notes, Boolean starred) {
        this.id = id;
        this.name = name;
        this.picturePaths = picturePaths;
        this.notes = notes;
        this.starred = starred;
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPicturePaths() { return picturePaths; }

    public void setPicturePaths(ArrayList<String> paths) { picturePaths = paths; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSource() { return this.source; }

    public void setSource(String source) { this.source = source; }

    public Boolean isStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
