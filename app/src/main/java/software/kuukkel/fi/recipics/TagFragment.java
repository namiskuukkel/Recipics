package software.kuukkel.fi.recipics;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.cunoraz.tagview.OnTagClickListener;
import com.cunoraz.tagview.TagView;
import com.cunoraz.tagview.Tag;

public class TagFragment extends Fragment {

    private TagView chosenTagGroup;
    private TagView tagGroup;
    View mahView;
    PreserveTags mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (PreserveTags) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        mCallback.SaveChosenTags(chosenTagGroup.getTags());
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCallback.SaveChosenTags(chosenTagGroup.getTags());
        Log.d("pause", "onPause");
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        chosenTagGroup.addTags(mCallback.GetTags());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tagGroup = new TagView(getActivity());
        chosenTagGroup = new TagView(getActivity());
        // Inflate the layout for this fragment
        mahView = inflater.inflate(R.layout.fragment_tag, container, false);
        addInitialTags();
        chosenTagGroup = (TagView) mahView.findViewById(R.id.chosen_tag_group);
        //set click listener
        tagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                tag.isDeletable = true;
                tagGroup.remove(position);
                chosenTagGroup.addTag(tag);
            }
        });
        chosenTagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                tag.isDeletable = false;
                chosenTagGroup.remove(position);
                //TODO: Put the tag back to where it was!
                tagGroup.addTag(tag);
            }
        });
        return mahView;
    }

    private void addInitialTags() {
        DBHelper db = new DBHelper(getActivity());
        ArrayList<software.kuukkel.fi.recipics.Tag> tags = db.getAllTags();
        for (software.kuukkel.fi.recipics.Tag t: tags ) {
            tagGroup = (TagView) mahView.findViewById(R.id.tag_group);
            Tag tag = new Tag(t.getName());
            tag.isDeletable = false;

            tag.layoutColor = Color.parseColor(t.getColor());
            //You can add one tag
            tagGroup.addTag(tag);
        }
        //You can add multiple tag via ArrayList
        //tagGroup.addTags();
        //Via string array
        //addTags(String[]tags);

        Tag tag = new Tag("+ Add tag");
        tag.isDeletable = false;
    }

    public static interface PreserveTags {
        public void SaveChosenTags(List<Tag> chosen);
        public List<Tag> GetTags();
    }
}
