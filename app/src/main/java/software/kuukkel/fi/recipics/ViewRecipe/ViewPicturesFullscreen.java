package software.kuukkel.fi.recipics.ViewRecipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import software.kuukkel.fi.recipics.HelperClass;
import software.kuukkel.fi.recipics.R;

public class ViewPicturesFullscreen extends AppCompatActivity {

    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    ArrayList<String> paths;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pictures_fullscreen);

        Intent intent = getIntent();
        //Get filepaths for the images associated with this recipe
        paths = intent.getStringArrayListExtra("paths");
        ImageView view = (ImageView) findViewById(R.id.imageView);
        Bitmap img = getRotatedImage(paths.get(0));
        if( img != null ) {
            view.setImageBitmap(img);
        }

        index = 0;
    }

    //http://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    ImageView view = (ImageView) findViewById(R.id.imageView);
                    //Left to right swipe: Moving to earlier pictures
                    if(deltaX > 0) {
                        //...If there are earlier pictures
                        if(index > 0) {
                            Bitmap img = getRotatedImage(paths.get(--index));
                            if( img != null) {
                                view.setImageBitmap(img);
                            }
                        }
                    //Right to left swipe: Moving to later pictures
                    } else {
                        //...If there are any left
                        if(index < paths.size()-1) {
                            Bitmap img = getRotatedImage(paths.get(++index));
                            if( img != null) {
                                view.setImageBitmap(img);
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private Bitmap getRotatedImage(String path) {
        try {
            Uri uri = Uri.fromFile(new File(path));
            Bitmap img = BitmapFactory.decodeFile(path);
            img = HelperClass.rotateImageIfRequired(img, uri);
            return img;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
