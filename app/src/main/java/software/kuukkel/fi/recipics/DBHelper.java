package software.kuukkel.fi.recipics;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import static software.kuukkel.fi.recipics.Tag.*;

import static software.kuukkel.fi.recipics.DBConst.*;

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

            ArrayList<Long> pathIds = new ArrayList<Long>();
            for (String path: recipe.getFilePaths()) {
                ContentValues PathContentValues = new ContentValues();
                PathContentValues.put(PathEntry.PATHS_COLUMN_PATH, path);
                pathIds.add(db.insert(PathEntry.PATHS_TABLE_NAME, null, PathContentValues));
            }

            for (long pathId : pathIds) {
                ContentValues RecipeToPathsValues = new ContentValues();
                RecipeToPathsValues.put(RecipeToPathEntry.RECIPETOPATH_COLUMN_PATHID, pathId);
                RecipeToPathsValues.put(RecipeToPathEntry.RECIPETOPATH_COLUMN_RECIPEID, recipeId);
                db.insert(RecipeToPathEntry.RECIPETOPATH_TABLE_NAME, null, RecipeToPathsValues);
            }

            //TODO Should be tag objects
            ArrayList<Long> tagIds = new ArrayList();
            for (Tag tag: tags) {
                ContentValues RecipeTagContentValues = new ContentValues();
                RecipeTagContentValues.put(RecipeToTagEntry.RECIPETOTAG_COLUMN_RECIPEID, recipeId);
                RecipeTagContentValues.put(RecipeToTagEntry.RECIPETOTAG_COLUMN_TAGID, tag.getName());
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
            Cursor res2 =  db.rawQuery( "SELECT " + PathEntry.PATHS_COLUMN_PATH + " FROM  " +
                    RecipeEntry.RECIPES_TABLE_NAME
                    + " r JOIN " + RecipeToPathEntry.RECIPETOPATH_TABLE_NAME
                    + " rp ON (r._id = rp." + RecipeToPathEntry.RECIPETOPATH_COLUMN_RECIPEID
                    + ") JOIN " + PathEntry.PATHS_TABLE_NAME + " p ON (p._id = rp." +
                    RecipeToPathEntry.RECIPETOPATH_COLUMN_PATHID + ")", null );

            res2.moveToFirst();
            ArrayList<String> paths = new ArrayList<>();
            while(res2.isAfterLast() == false){
                paths.add(res.getString(res2.getInt(0)));
                res2.moveToNext();
            }
            tmp.setUrisFromPaths(paths);
            recipes.add(tmp);
            res.moveToNext();
        }
        db.close();
        return recipes;
    }

    public ArrayList<Tag> getAllTags()
    {
        ArrayList<Tag> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TagEntry.TAGS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Tag(res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_NAME )),
                    res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_COLOR))));
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    public void saveRecipe(Recipe id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(5) } );

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
                long id = db.insert(TagEntry.TAGS_TABLE_NAME, null, contentValues);
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