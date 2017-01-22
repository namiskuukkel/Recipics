package software.kuukkel.fi.recipics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.viewRecipe);
        DBHelper db = new DBHelper(this);
        ArrayList<Recipe> recipes = db.getAllRecipes();

        for(Recipe recipe: recipes) {
            LinearLayout lLayout = new LinearLayout(this);
            TextView t = new TextView(this);
            t.setText(recipe.getName());
            lLayout.addView(t);
            ImageView image = new ImageView(this);
            try {
                Log.d("path", recipe.getPictureUris().get(0).getPath());
                image.setImageBitmap(
                        HelperClass.handleSamplingAndRotationBitmap(this, recipe.getPictureUris().get(0)));
            } catch (Exception e ) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 4;

            final Bitmap bitmap = BitmapFactory.decodeFile(recipe.getFilePaths().get(0),
                    options);
            image.setImageBitmap(bitmap);
            lLayout.addView(image);
            parentLayout.addView(lLayout);
        }
    }
}
