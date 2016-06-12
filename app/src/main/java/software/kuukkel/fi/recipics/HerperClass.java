package software.kuukkel.fi.recipics;

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
