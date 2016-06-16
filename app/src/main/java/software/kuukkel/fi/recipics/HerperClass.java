package software.kuukkel.fi.recipics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import java.io.File;
import android.net.Uri;
import android.util.Log;

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

    static Bitmap getPreview(Uri uri) {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
            return null;

        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        return BitmapFactory.decodeFile(uri.getPath(), opts);
    }
}
