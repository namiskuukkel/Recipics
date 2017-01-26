package software.kuukkel.fi.recipics.Database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import software.kuukkel.fi.recipics.Objects.Recipe;
import software.kuukkel.fi.recipics.Objects.Tag;

import static software.kuukkel.fi.recipics.Database.DBConst.*;
import static software.kuukkel.fi.recipics.Objects.Tag.Type.DISH;

/**
 * Created by namiskuukkel on 6.6.2016.
 */


public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_TAGS);
        db.execSQL(CREATE_TABLE_PATHS);
        db.execSQL(CREATE_TABLE_RECIPETOTAG);
        db.execSQL(CREATE_TABLE_RECIPETOPATH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Can these be performed with a single exec call?
        db.execSQL(DROP_TABLE + RecipeEntry.RECIPES_TABLE_NAME);
        db.execSQL(DROP_TABLE + TagEntry.TAGS_TABLE_NAME);
        db.execSQL(DROP_TABLE + PathEntry.PATHS_TABLE_NAME);
        db.execSQL(DROP_TABLE + RecipeToPathEntry.RECIPETOPATH_TABLE_NAME);
        db.execSQL(DROP_TABLE + RecipeToTagEntry.RECIPETOTAG_TABLE_NAME);
        onCreate(db);
    }

    //TODO: Remove the usage and add create default tags
    public void chickenDestroy() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_TABLE + RecipeEntry.RECIPES_TABLE_NAME);
        db.execSQL(DROP_TABLE + TagEntry.TAGS_TABLE_NAME);
        db.execSQL(DROP_TABLE + PathEntry.PATHS_TABLE_NAME);
        db.execSQL(DROP_TABLE + RecipeToPathEntry.RECIPETOPATH_TABLE_NAME);
        db.execSQL(DROP_TABLE + RecipeToTagEntry.RECIPETOTAG_TABLE_NAME);
        onCreate(db);
    }
    public boolean insertRecipe (Recipe recipe, ArrayList<Tag> tags)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues RecipeContentValues = new ContentValues();
            RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_NAME, recipe.getName());
            RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_NOTES, recipe.getNotes());

            //SQLite as no boolean
            int starredInt = 0;
            if(recipe.isStarred()) {
                starredInt = 1;
            }
            RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_STARRED, starredInt);

            RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_SOURCE, recipe.getSource());

            long recipeId = db.insert(RecipeEntry.RECIPES_TABLE_NAME, null, RecipeContentValues);

            //Create paths for the recipe
            ArrayList<Long> pathIds = new ArrayList<Long>();
            for (String path: recipe.getPicturePaths()) {
                ContentValues PathContentValues = new ContentValues();
                PathContentValues.put(PathEntry.PATHS_COLUMN_PATH, path);
                pathIds.add(db.insert(PathEntry.PATHS_TABLE_NAME, null, PathContentValues));
            }

            //Connect the new paths with the recipe on RecipeToPath
            for (long pathId : pathIds) {
                ContentValues RecipeToPathsValues = new ContentValues();
                RecipeToPathsValues.put(RecipeToPathEntry.RECIPETOPATH_COLUMN_PATHID, pathId);
                RecipeToPathsValues.put(RecipeToPathEntry.RECIPETOPATH_COLUMN_RECIPEID, recipeId);
                db.insert(RecipeToPathEntry.RECIPETOPATH_TABLE_NAME, null, RecipeToPathsValues);
            }

            //Create RecipeToTag entries for the Recipe
            ArrayList<Long> tagIds = new ArrayList();
            for (Tag tag: tags) {
                ContentValues RecipeTagContentValues = new ContentValues();
                RecipeTagContentValues.put(RecipeToTagEntry.RECIPETOTAG_COLUMN_RECIPEID, recipeId);
                RecipeTagContentValues.put(RecipeToTagEntry.RECIPETOTAG_COLUMN_TAGNAME, tag.getName());
                tagIds.add(db.insert(RecipeToTagEntry.RECIPETOTAG_TABLE_NAME, null, RecipeTagContentValues));
            }
            db.setTransactionSuccessful();
        } catch(Exception ex ) {
            Log.d("Db ex", ex.toString());
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }

    public Cursor getRecipe(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + RecipeEntry.RECIPES_TABLE_NAME + "where id="+id+"", null );
        db.close();
        return res;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, RecipeEntry.RECIPES_TABLE_NAME);
        db.close();
        return numRows;
    }

    /*public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        db.close();
        return true;
    }*/

    public Integer deleteRecipe (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Recipe> getAllRecipes()
    {
        ArrayList<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM  " + RecipeEntry.RECIPES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Recipe tmp = new Recipe();
            tmp.setId(res.getInt(res.getColumnIndex(RecipeEntry._ID)));
            tmp.setName(res.getString(res.getColumnIndex(RecipeEntry.RECIPES_COLUMN_NAME)));
            tmp.setNotes(res.getString(res.getColumnIndex(RecipeEntry.RECIPES_COLUMN_NOTES)));
            tmp.setSource(res.getString(res.getColumnIndex(RecipeEntry.RECIPES_COLUMN_SOURCE)));
            int starred = res.getInt(res.getColumnIndex(RecipeEntry.RECIPES_COLUMN_STARRED));
            if(starred == 1) {
                tmp.setStarred(true);
            } else {
                tmp.setStarred(false);
            }

            tmp.setPicturePaths(getFilePathsForRecipe(db, tmp.getId()));
            tmp.setTags(getTagsForRecipe(db, tmp.getId()));
            recipes.add(tmp);
            res.moveToNext();
        }
        res.close();
        db.close();
        return recipes;
    }

    public ArrayList<String> getFilePathsForRecipe(SQLiteDatabase db, int id) {
        Cursor res =  db.rawQuery( "SELECT " + PathEntry.PATHS_COLUMN_PATH + " FROM  " +
                RecipeEntry.RECIPES_TABLE_NAME
                + " r JOIN " + RecipeToPathEntry.RECIPETOPATH_TABLE_NAME
                + " rp ON (r._id = rp." + RecipeToPathEntry.RECIPETOPATH_COLUMN_RECIPEID
                + ") JOIN " + PathEntry.PATHS_TABLE_NAME + " p ON (p._id = rp." +
                RecipeToPathEntry.RECIPETOPATH_COLUMN_PATHID + ") WHERE r._id = " +
                id, null );

        res.moveToFirst();
        ArrayList<String> paths = new ArrayList<>();
        while(res.isAfterLast() == false){
            paths.add(res.getString(0));
            res.moveToNext();
        }
        return paths;
    }

    public ArrayList<Tag> getTagsForRecipe(SQLiteDatabase db, int id) {
        Cursor res =  db.rawQuery( "SELECT * FROM  " +
                RecipeEntry.RECIPES_TABLE_NAME
                + " r JOIN " + RecipeToTagEntry.RECIPETOTAG_TABLE_NAME
                + " rt ON (r._id = rt." + RecipeToTagEntry.RECIPETOTAG_COLUMN_RECIPEID
                + ") JOIN " + TagEntry.TAGS_TABLE_NAME + " t ON (t." + TagEntry.TAGS_COLUMN_NAME +
                " = rt." + RecipeToTagEntry.RECIPETOTAG_COLUMN_TAGNAME + ") WHERE r._id = " +
                id, null );

        res.moveToFirst();
        ArrayList<Tag> tags = new ArrayList<>();
        while(res.isAfterLast() == false){
            tags.add(new Tag(res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_NAME )),
                    res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_COLOR)),
                    stringToTagType(res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_TYPE))),
                    res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_PARENT_TAG))));
            res.moveToNext();
        }
        res.close();
        return tags;
    }

    public ArrayList<Tag> getAllTags()
    {
        ArrayList<Tag> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TagEntry.TAGS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Tag(res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_NAME )),
                    res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_COLOR)),
                    stringToTagType(res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_TYPE))),
                    res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_PARENT_TAG))));
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }

    public Tag.Type stringToTagType(String type) {
        switch (type) {
            case "dish":
                return DISH;
            case "ingredient":
                return Tag.Type.INGREDIENT;
            case "origin":
                return Tag.Type.ORIGIN;
            case "swiftness":
                return Tag.Type.SWIFTNESS;
            default:
                return Tag.Type.UNDEFINED;
        }
    }

    public String tagTypeToString(Tag.Type type) {
        switch (type) {
            case DISH:
                return "dish";
            case INGREDIENT:
                return "ingredient";
            case ORIGIN:
                return "origin";
            case SWIFTNESS:
                return "swiftness";
            default:
                return "undefined";
        }
    }

    public void saveRecipe(Recipe id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(5) } );

    }

    public Boolean insertTag(Tag tag) {
        //Check if there already is a tag by that name
        if(tagExists(tag.getName())) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagEntry.TAGS_COLUMN_NAME, tag.getName());
        contentValues.put(TagEntry.TAGS_COLUMN_COLOR, tag.getColor());
        contentValues.put(TagEntry.TAGS_COLUMN_TYPE, tagTypeToString(tag.getType()));
        contentValues.put(TagEntry.TAGS_COLUMN_PARENT_TAG, tag.getParentCategory());
        db.insert(TagEntry.TAGS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    private Boolean tagExists(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectString = "SELECT * FROM " + TagEntry.TAGS_TABLE_NAME + " WHERE " +
                TagEntry.TAGS_COLUMN_NAME + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {name});

        boolean hasObject = false;
        if(cursor.moveToFirst()) {
            hasObject = true;
        }
        cursor.close();
        db.close();
        return hasObject;
    }

    //TODO: Insert tag; check that a tag with the new tag name doesn't already exist
    public void insertDefaultTags(Tag[] tags){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            for ( Tag tag : tags) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TagEntry.TAGS_COLUMN_NAME, tag.getName());
                contentValues.put(TagEntry.TAGS_COLUMN_COLOR, tag.getColor());
                contentValues.put(TagEntry.TAGS_COLUMN_TYPE, tagTypeToString(tag.getType()));
                contentValues.put(TagEntry.TAGS_COLUMN_PARENT_TAG, tag.getParentCategory());
                db.insert(TagEntry.TAGS_TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch(Exception ex ) {
            Log.d("Db ex", ex.toString());
        } finally {
            db.endTransaction();
            db.close();
        }
        db.close();
    }
}