package software.kuukkel.fi.recipics;

import android.net.Uri;
import android.provider.BaseColumns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by namiskuukkel on 6.6.2016.
 */
public class Recipe {

    private int id;
    private String name = "";
    //Filepaths of the pictures connected to the recipe
    private ArrayList<Uri> pictureUris;
    private String notes = "";
    private Boolean starred = false;
    private String source = "";
    private ArrayList<Tag> tags;

    public Recipe() {
        pictureUris = new ArrayList<>();
    }

    public Recipe( int id, String name, ArrayList<Uri> pictureUris, String notes, Boolean starred) {
        this.id = id;
        this.name = name;
        this.pictureUris = pictureUris;
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

    public ArrayList<Uri> getPictureUris() { return pictureUris; }

    public void setPictureUris(ArrayList<Uri> uris) { pictureUris = uris; }

    public void setUrisFromPaths(ArrayList<String> paths) {
        for(String path: paths) {
            pictureUris.add(Uri.fromFile(new File(path)));
        }
    }

    public ArrayList<String> getFilePaths() {
        ArrayList<String> paths = new ArrayList<>();
        for(Uri uri: this.pictureUris ) {
            paths.add(uri.getPath());
        }
        return paths;
    }

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
}
