package software.kuukkel.fi.recipics;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.cunoraz.tagview.TagView;
//Based on: https://thepseudocoder.wordpress.com/2011/10/05/android-page-swiping-using-viewpager/

/**
 * The <code>ViewPagerFragmentActivity</code> class is the fragment activity hosting the ViewPager
 * @author mwho
 */
public class ViewPagerFragmentActivity extends FragmentActivity
        implements TagFragment.PreserveTags, CameraFragment.PreserveFilepaths {

    List<com.cunoraz.tagview.Tag> chosenTags;
    List<com.cunoraz.tagview.Tag> otherTags;
    List<Fragment> fragments;
    Recipe recipe;
    /** maintains the pager adapter*/
    private PagerAdapter mPagerAdapter;
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.fragment_view_pager);

        recipe = new Recipe();

        //initialse the pager
        this.initialisePaging();
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, CameraFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RecipeDetailsFillActivity.class.getName()));
        fragments.add(Fragment.instantiate(this, TagFragment.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
    }

    public void saveRecipe(View view) {
        //recipe = fragments.get(0).getRecipe();
    }

    public void SaveChosenTags(List<com.cunoraz.tagview.Tag> chosen, List<com.cunoraz.tagview.Tag> notChosen) {
        chosenTags = chosen;
        otherTags = notChosen;
    }

    public List<List<com.cunoraz.tagview.Tag>> GetTags() {
        if(chosenTags == null || otherTags == null ) {
            chosenTags = new ArrayList<>();
            otherTags = new ArrayList<>();

            DBHelper db = new DBHelper(this);
            ArrayList<Tag> tags = db.getAllTags();
            for (Tag t : tags) {
                otherTags.add(t.getTag());
            }
        }
        List<List<com.cunoraz.tagview.Tag>> tags = new ArrayList<>();
        tags.add(chosenTags);
        tags.add(otherTags);
        return tags;
    }

    public void savePictureFilepaths(ArrayList<String> path) { recipe.setPathsToPictures(path); }

    public ArrayList<String> getPictureFilepaths() { return recipe.getPathsToPictures(); }

}