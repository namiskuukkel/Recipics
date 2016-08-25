package software.kuukkel.fi.recipics;

import android.app.Fragment;
import android.graphics.Color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import com.cunoraz.tagview.OnTagClickListener;
import com.cunoraz.tagview.OnTagDeleteListener;
import com.cunoraz.tagview.TagView;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.Constants;

public class RecipeDetailsFillActivity extends Fragment {

    private TagView tagGroup;
    private ArrayList< Tag > tagList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        tagGroup = new TagView(getActivity());
        addTag();

        //set click listener
        tagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
            }
        });

        return (LinearLayout)inflater.inflate(R.layout.activity_camera, container, false);
    }

    public void addTag() {
        DBHelper db = new DBHelper(getActivity());
        ArrayList<software.kuukkel.fi.recipics.Tag> tags = db.getAllTags();
        for (software.kuukkel.fi.recipics.Tag t: tags ) {
            TagView tagGroup = (TagView) getView().findViewById(R.id.tag_group);
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
