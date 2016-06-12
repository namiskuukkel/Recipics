package software.kuukkel.fi.recipics;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
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
        onCreate(db);
    }

    public boolean insertRecipe  (String name, String[] paths, String notes, Boolean starred,
                                  String source, String[] tags)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues RecipeContentValues = new ContentValues();
        RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_NAME, name);
        if(notes != "") {
            RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_NOTES, notes);
        }
        //SQLite as no boolean
        int starredInt = 0;
        if(starred) {
            starredInt = 1;
        }
        RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_STARRED, starredInt);
        if(source != "") {
            RecipeContentValues.put(RecipeEntry.RECIPES_COLUMN_SOURCE, source);
        }
        long recipeId = db.insert(RecipeEntry.RECIPES_TABLE_NAME, null, RecipeContentValues);

        ArrayList<Long> pathIds = new ArrayList<Long>();
        for (String path: paths) {
            ContentValues PathContentValues = new ContentValues();
            PathContentValues.put(PathEntry.PATHS_COLUMN_PATH, path);
            pathIds.add(db.insert(PathEntry.PATHS_TABLE_NAME, null, PathContentValues));
        }

        for (long pathId : pathIds) {
            ContentValues RecipeToPathsValues = new ContentValues();
            RecipeToPathsValues.put(RecipeToPathEntry.RECIPETOPATH_COLUMN_RECIPEID, recipeId);
            RecipeToPathsValues.put(RecipeToPathEntry.RECIPETOPATH_COLUMN_PATHID, pathId);
            db.insert(RecipeToPathEntry.RECIPETOPATH_TABLE_NAME, null, RecipeToPathsValues);
        }

        //TODO Should be tag objects
        ArrayList<Long> tagIds = new ArrayList();
        for (String tag: tags) {
            ContentValues TagContentValues = new ContentValues();
            TagContentValues.put(TagEntry.TAGS_COLUMN_NAME, tag);
            tagIds.add(db.insert(PathEntry.PATHS_TABLE_NAME, null, TagContentValues));
        }

        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, RecipeEntry.RECIPES_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteRecipe (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllRecipes()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(RecipeEntry.RECIPES_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Tag> getAllTags()
    {
        ArrayList<Tag> array_list = new ArrayList<Tag>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TagEntry.TAGS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Tag(res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_NAME )),
                    res.getString(res.getColumnIndex(TagEntry.TAGS_COLUMN_COLOR))));
            res.moveToNext();
        }
        return array_list;
    }

    public void insertDefaultTags(Tag[] tags){
        SQLiteDatabase db = this.getWritableDatabase();
        for ( Tag tag : tags) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TagEntry.TAGS_COLUMN_NAME, tag.getName());
            contentValues.put(TagEntry.TAGS_COLUMN_COLOR, tag.getColor());
            long id = db.insert(TagEntry.TAGS_TABLE_NAME, null, contentValues);
            tag.setId(id);
        }

    }
}