package software.kuukkel.fi.recipics;

import android.support.v4.app.Fragment;
import android.graphics.Color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import com.cunoraz.tagview.OnTagClickListener;
import com.cunoraz.tagview.TagView;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.Constants;

public class RecipeDetailsFillActivity extends Fragment {


    View mahView;

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

        mahView = inflater.inflate(R.layout.fragment_recipe_details_fill, container, false);

        return mahView;
    }

}
