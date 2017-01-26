package software.kuukkel.fi.recipics.ViewRecipe;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import software.kuukkel.fi.recipics.Database.DBHelper;
import software.kuukkel.fi.recipics.HelperClass;
import software.kuukkel.fi.recipics.Objects.Recipe;
import software.kuukkel.fi.recipics.R;

public class ViewRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.viewRecipe);
        DBHelper db = new DBHelper(this);
        ArrayList<Recipe> recipes = db.getAllRecipes();

        for(final Recipe recipe: recipes) {
            LinearLayout lLayout = new LinearLayout(this);
            TextView t = new TextView(this);
            t.setText(recipe.getName());
            t.setClickable(true);
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ViewRecipe.this, ViewPicturesFullscreen.class);
                    intent.putExtra("paths", recipe.getPicturePaths());
                    startActivity(intent);
                }
            });
            lLayout.addView(t);
            parentLayout.addView(lLayout);
        }
    }
}
