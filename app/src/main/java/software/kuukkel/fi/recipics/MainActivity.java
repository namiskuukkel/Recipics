package software.kuukkel.fi.recipics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        checkFirstRun();

        /*DBHelper db = new DBHelper(this);

        db.chickenDestroy();

        Tag[] defaultTags = HelperClass.createDefaultTags();
        db.insertDefaultTags(defaultTags);*/

        Button newButton= (Button) findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewPagerFragmentActivity.class));
            }
        });

        Button button= (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewRecipe.class));
            }
        });
    }

    //http://stackoverflow.com/questions/7217578/check-if-application-is-on-its-first-run
    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            if ( !isExternalStorageWritable()) {
                Toast.makeText(MainActivity.this, "A new directory could not be created", Toast.LENGTH_SHORT).show();
            }
            // Get the directory for the user's public pictures directory.
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
            if (!file.mkdirs()) {
                Log.e("Dir create", "Directory not created");
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("dir_name", file.getAbsolutePath());
            editor.commit();

            //Database creation
            DBHelper db = new DBHelper(this);
            Tag[] defaultTags = HelperClass.createDefaultTags();
            db.insertDefaultTags(defaultTags);

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }
    //http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
