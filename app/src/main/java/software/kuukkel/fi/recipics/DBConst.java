package software.kuukkel.fi.recipics;

import android.provider.BaseColumns;

import java.security.PublicKey;

/**
 * Created by namiskuukkel on 12.6.2016.
 */

public class DBConst {
    public static final String DATABASE_NAME = "Recipics.db";
    public static final String CREATE = "CREATE TABLE ";
    public static final String PRIMARY_KEY = " PRIMARY KEY";
    public static final String AUTOINCREMENT = " AUTOINCREMENT";
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

    public static final String CREATE_TABLE_RECIPES = CREATE + RecipeEntry.RECIPES_TABLE_NAME +
            " (" + RecipeEntry._ID + NUMBER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP
            + RecipeEntry.RECIPES_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            RecipeEntry.RECIPES_COLUMN_NOTES + TEXT_TYPE + COMMA_SEP +
            RecipeEntry.RECIPES_COLUMN_STARRED + NUMBER_TYPE + COMMA_SEP +
            RecipeEntry.RECIPES_COLUMN_SOURCE + TEXT_TYPE +")";

    public static abstract class TagEntry implements BaseColumns {
        public static final String TAGS_TABLE_NAME = "tags";
        public static final String TAGS_COLUMN_NAME = "name";
        public static final String TAGS_COLUMN_COLOR = "color";
    }

    public static final String CREATE_TABLE_TAGS = CREATE + TagEntry.TAGS_TABLE_NAME +
            " (" + TagEntry.TAGS_COLUMN_NAME + TEXT_TYPE + PRIMARY_KEY + COMMA_SEP
            + TagEntry.TAGS_COLUMN_COLOR + TEXT_TYPE + ")";

    public static abstract class PathEntry implements BaseColumns {
        public static final String PATHS_TABLE_NAME = "paths";
        public static final String PATHS_COLUMN_PATH = "path";
    }

    public static final String CREATE_TABLE_PATHS = CREATE + PathEntry.PATHS_TABLE_NAME +
            " (" + PathEntry._ID + NUMBER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            PathEntry.PATHS_COLUMN_PATH + ")";


    public static abstract class RecipeToTagEntry implements BaseColumns {
        public static final String RECIPETOTAG_TABLE_NAME = "RecipeToTag";
        //TODO: Is this required?
        public static final String RECIPETOTAG_COLUMN_ID = "id";
        public static final String RECIPETOTAG_COLUMN_RECIPEID = "recipeid";
        public static final String RECIPETOTAG_COLUMN_TAGID = "tagid";
    }

    public static final String CREATE_TABLE_RECIPETOTAG = CREATE +
            RecipeToTagEntry.RECIPETOTAG_TABLE_NAME + " (" +
            RecipeToTagEntry._ID + NUMBER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            RecipeToTagEntry.RECIPETOTAG_COLUMN_RECIPEID + NUMBER_TYPE + COMMA_SEP +
            RecipeToTagEntry.RECIPETOTAG_COLUMN_TAGID + TEXT_TYPE + ")";

    public static abstract class RecipeToPathEntry implements BaseColumns {
        public static final String RECIPETOPATH_TABLE_NAME = "RecipeToPath";
        public static final String RECIPETOPATH_COLUMN_RECIPEID = "recipeid";
        public static final String RECIPETOPATH_COLUMN_PATHID = "pathid";
    }

    public static final String CREATE_TABLE_RECIPETOPATH = CREATE  +
            RecipeToPathEntry.RECIPETOPATH_TABLE_NAME + " (" +
            RecipeToPathEntry._ID + NUMBER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            RecipeToPathEntry.RECIPETOPATH_COLUMN_RECIPEID + NUMBER_TYPE + COMMA_SEP +
            RecipeToPathEntry.RECIPETOPATH_COLUMN_PATHID + NUMBER_TYPE + ")";
}
