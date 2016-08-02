package software.kuukkel.fi.recipics;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import com.cunoraz.tagview.OnTagClickListener;
import com.cunoraz.tagview.OnTagDeleteListener;
import com.cunoraz.tagview.TagView;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.Constants;

public class RecipeDetailsFillActivity extends AppCompatActivity {

    private TagView tagGroup;
    private ArrayList< Tag > tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details_fill);

        tagGroup = new TagView(this);
        addTag();

        //set click listener
        tagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
            }
        });

        //set delete listener
        tagGroup.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
            }
        });
    }

    public void addTag() {
        DBHelper db = new DBHelper(this);
        ArrayList<software.kuukkel.fi.recipics.Tag> tags = db.getAllTags();
        for (software.kuukkel.fi.recipics.Tag t: tags ) {
            TagView tagGroup = (TagView) findViewById(R.id.tag_group);
            Tag tag = new Tag(t.getName());
            tag.isDeletable = true;
            tag.layoutColor = Color.parseColor(t.getColor());
            //You can add one tag
            tagGroup.addTag(tag);
        }
        //You can add multiple tag via ArrayList
        //tagGroup.addTags();
        //Via string array
        //addTags(String[]tags);
    }
    private void prepareTags() {
        tagList = new ArrayList<>();
        JSONArray jsonArray;
        JSONObject temp;
        try {
            jsonArray = new JSONArray(Constants.COUNTRIES);
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getJSONObject(i);
                tagList.add(new Tag("kukkuu"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
