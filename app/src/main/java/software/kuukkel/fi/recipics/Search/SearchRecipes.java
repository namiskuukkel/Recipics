package software.kuukkel.fi.recipics.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

import software.kuukkel.fi.recipics.Database.DBHelper;
import software.kuukkel.fi.recipics.Objects.Recipe;
import software.kuukkel.fi.recipics.R;

public class SearchRecipes extends AppCompatActivity {

    private AutoCompleteTextView searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);

        searchField = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        DBHelper db = new DBHelper(this);
        ArrayList<Recipe> recipes = db.getAllRecipes();
        ArrayList<String> recipeNames = new ArrayList<>();
        for( Recipe recipe: recipes) {
            recipeNames.add(recipe.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,recipeNames);
        searchField.setAdapter(adapter);
    }
}
