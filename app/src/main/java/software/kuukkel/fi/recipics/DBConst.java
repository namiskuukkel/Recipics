package software.kuukkel.fi.recipics;

import android.provider.BaseColumns;

/**
 * Created by namiskuukkel on 12.6.2016.
 */

public class DBConst {
    public static final String DATABASE_NAME = "Recipics.db";

    public static final String TEXT_TYPE = " TEXT";
    public static final String NUMBER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    public static abstract class RecipeEntry implements BaseColumns {
        public static final String RECIPES_TABLE_NAME = "recipes";
        //TODO: Is this required?
        public static final String RECIPES_COLUMN_ID = "id";
        public static final String RECIPES_COLUMN_NAME = "name";
        public static final String RECIPES_COLUMN_NOTES = "notes";
        public static final String RECIPES_COLUMN_STARRED = "starred";
        public static final String RECIPES_COLUMN_SOURCE = "source";
    }

    public static final String CREATE_TABLE_RECIPES = "create table " + RecipeEntry.RECIPES_TABLE_NAME +
            " (" + RecipeEntry._ID + NUMBER_TYPE + " primary key" + COMMA_SEP
            + RecipeEntry.RECIPES_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            RecipeEntry.RECIPES_COLUMN_NOTES + TEXT_TYPE + COMMA_SEP +
            RecipeEntry.RECIPES_COLUMN_STARRED + NUMBER_TYPE + COMMA_SEP +
            RecipeEntry.RECIPES_COLUMN_SOURCE + TEXT_TYPE +")";

    public static abstract class TagEntry implements BaseColumns {
        public static final String TAGS_TABLE_NAME = "tags";
        //TODO: Is this required?
        public static final String TAGS_COLUMN_ID = "id";
        public static final String TAGS_COLUMN_NAME = "name";
        public static final String TAGS_COLUMN_COLOR = "color";
    }

    public static final String CREATE_TABLE_TAGS = "create table " + TagEntry.TAGS_TABLE_NAME +
            " (" + TagEntry.TAGS_COLUMN_NAME + TEXT_TYPE + " primary key" + COMMA_SEP
            + TagEntry.TAGS_COLUMN_COLOR + TEXT_TYPE + ")";


    public static abstract class PathEntry implements BaseColumns {
        public static final String PATHS_TABLE_NAME = "paths";
        //TODO: Is this required?
        public static final String PATHS_COLUMN_ID = "id";
        public static final String PATHS_COLUMN_PATH = "path";
    }

    public static final String CREATE_TABLE_PATHS = "create table " + PathEntry.PATHS_TABLE_NAME +
            " (" + PathEntry._ID + NUMBER_TYPE + " primary key" + COMMA_SEP +
            PathEntry.PATHS_COLUMN_PATH + ")";


    public static abstract class RecipeToTagEntry implements BaseColumns {
        public static final String RECIPETOTAG_TABLE_NAME = "RecipeToTag";
        //TODO: Is this required?
        public static final String RECIPETOTAG_COLUMN_ID = "id";
        public static final String RECIPETOTAG_COLUMN_RECIPEID = "recipeid";
        public static final String RECIPETOTAG_COLUMN_TAGID = "tagid";
    }

    public static final String CREATE_TABLE_RECIPETOTAG = "create table " +
            RecipeToTagEntry.RECIPETOTAG_TABLE_NAME + " (" + RecipeToTagEntry._ID +
            RecipeToTagEntry.RECIPETOTAG_COLUMN_RECIPEID + NUMBER_TYPE + ")";

    public static abstract class RecipeToPathEntry implements BaseColumns {
        public static final String RECIPETOPATH_TABLE_NAME = "RecipeToPath";
        //TODO: Is this required?
        public static final String RECIPETOPATH_COLUMN_ID = "id";
        public static final String RECIPETOPATH_COLUMN_RECIPEID = "recipeid";
        public static final String RECIPETOPATH_COLUMN_PATHID = "pathid";
    }

    public static final String CREATE_TABLE_RECIPETOPATH = "create table " +
            RecipeToPathEntry.RECIPETOPATH_TABLE_NAME + " (" + RecipeToPathEntry._ID +
            RecipeToPathEntry.RECIPETOPATH_COLUMN_RECIPEID + NUMBER_TYPE + ")";
}
