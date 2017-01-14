package software.kuukkel.fi.recipics;

import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by namiskuukkel on 6.6.2016.
 */
public class Recipe {

    private int id;
    private String name;
    //Filepaths of the pictures connected to the recipe
    private ArrayList<String> pathsToPictures;
    private String notes;
    private Boolean starred;
    private String source;
    private Tag[] tags;

    public Recipe() {}

    public Recipe( int id, String name, ArrayList<String> pathsToPictures, String notes, Boolean starred) {
        this.id = id;
        this.name = name;
        this.pathsToPictures = pathsToPictures;
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

    public ArrayList<String> getPathsToPictures() { return pathsToPictures; }

    public void setPathsToPictures(ArrayList<String> paths) { pathsToPictures = paths; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean isStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }
}
