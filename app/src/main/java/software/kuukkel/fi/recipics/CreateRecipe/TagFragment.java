package software.kuukkel.fi.recipics.CreateRecipe;

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

//See https://android-arsenal.com/details/1/2566 for TagView
import com.cunoraz.tagview.OnTagClickListener;
import com.cunoraz.tagview.TagView;
import com.cunoraz.tagview.Tag;

import software.kuukkel.fi.recipics.R;

//TODO: At which lifecycle phase should the chosen tags be saved to parent activity?
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
    public void onPause() {
        super.onPause();
        mCallback.SaveChosenTags(chosenTagGroup.getTags(), tagGroup.getTags());
        Log.d("pause", "onPause: Tag");
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        if(state != null ) {
            Log.d("bundle", "Now it wasn't null");
        }
        /*List<List<Tag>> tags = mCallback.GetTags();
        if(tags != null) {
            if(tags.get(0) != null && tags.get(1) != null) {
                chosenTagGroup.addTags(tags.get(0));
                tagGroup.addTags(tags.get(1));
            }
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tagGroup = new TagView(getActivity());
        chosenTagGroup = new TagView(getActivity());

        // Inflate the layout for this fragment
        mahView = inflater.inflate(R.layout.fragment_tag, container, false);

        if(savedInstanceState != null ) {
            Log.d("bundle", "Now it wasn't null");
        }

        tagGroup = (TagView) mahView.findViewById(R.id.tag_group);
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

        List<List<Tag>> tags = mCallback.GetTags();
        if(tags != null) {
            if(tags.get(0) != null && tags.get(1) != null) {
                for(Tag t: tags.get(0)) {
                    chosenTagGroup.addTag(t);
                }
                for(Tag t: tags.get(1)) {
                    tagGroup.addTag(t);
                }
            }
        }
        /*TODO: Add this somewhere smart. This should open a view where the user can add a new tag,
         name it, give it a type and a possible parent tag*/
        /*Tag tag = new Tag("+ Add tag");
        tag.isDeletable = false;
        tagGroup.addTag(tag);*/

        return mahView;
    }

    public List<Tag> getChosenTags() {
        if(chosenTagGroup != null) {
            return chosenTagGroup.getTags();
        }
        else { return new ArrayList<Tag>(); }
    }

    /*Interface to save tags chosen by the user when fragment is killed and to get them back once
    fragment continues */
    public static interface PreserveTags {
        public void SaveChosenTags(List<Tag> chosen, List<Tag> notChosen);
        public List<List<Tag>> GetTags();
    }
}
