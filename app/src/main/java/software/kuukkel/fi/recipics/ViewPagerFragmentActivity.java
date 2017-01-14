package software.kuukkel.fi.recipics;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * The <code>ViewPagerFragmentActivity</code> class is the fragment activity hosting the ViewPager
 * @author mwho
 */
public class ViewPagerFragmentActivity extends FragmentActivity
        implements TagFragment.PreserveTags, CameraFragment.PreserveFilepaths {

    List<com.cunoraz.tagview.Tag> chosenTags;
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
        //initialsie the pager
        this.initialisePaging();

        recipe = new Recipe();
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, CameraFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RecipeDetailsFillActivity.class.getName()));
        fragments.add(Fragment.instantiate(this, TagFragment.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
    }

    public void SaveChosenTags(List<com.cunoraz.tagview.Tag> chosen) {
        chosenTags = chosen;
    }

    public List<com.cunoraz.tagview.Tag> GetTags() {
        return chosenTags;
    }

    public void savePictureFilepaths(ArrayList<String> path) { recipe.setPathsToPictures(path); }

    public ArrayList<String> getPictureFilepaths() { return recipe.getPathsToPictures(); }

}