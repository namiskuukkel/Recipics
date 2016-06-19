package software.kuukkel.fi.recipics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import java.io.File;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static software.kuukkel.fi.recipics.Tag.Type.DISH;

/**
 * Created by namiskuukkel on 8.6.2016.
 */
public class HerperClass {


    static Tag[] createDefaultTags() {
        Tag[] tags = {new Tag("Main dish", "#5977FF", DISH), new Tag("Dessert", "#BEDDED", DISH),
                new Tag("Side", "#BBDBD1", DISH), new Tag("Snack", "#7E78D2", DISH)};
        return tags;
    }


}
