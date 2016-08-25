package software.kuukkel.fi.recipics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import com.cunoraz.tagview.OnTagClickListener;
import com.cunoraz.tagview.TagView;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

public class TagFragment extends Fragment {

    private TagView chosenTagGroup;
    private TagView tagGroup;
    private ArrayList< Tag > tagList;
    View mahView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tagGroup = new TagView(getActivity());
        chosenTagGroup = new TagView(getActivity());
        // Inflate the layout for this fragment
        mahView = inflater.inflate(R.layout.fragment_tag, container, false);
        addInitialTags();
        //set click listener
        tagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                tag.isDeletable = true;
            }
        });
        return mahView;
    }

    private void addInitialTags() {
        DBHelper db = new DBHelper(getActivity());
        ArrayList<software.kuukkel.fi.recipics.Tag> tags = db.getAllTags();
        for (software.kuukkel.fi.recipics.Tag t: tags ) {
            tagGroup = (TagView) mahView.findViewById(R.id.chosen_tag_group);
            Tag tag = new Tag(t.getName());

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
