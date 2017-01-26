package software.kuukkel.fi.recipics.ViewRecipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

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
        paths = intent.getStringArrayListExtra("paths");
        ImageView view = (ImageView) findViewById(R.id.imageView);
        Bitmap img = BitmapFactory.decodeFile(paths.get(0));
        index = 0;
        view.setImageBitmap(img);
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
                    if(deltaX > 0) {
                        if(index > 0) {
                            Bitmap img = BitmapFactory.decodeFile(paths.get(--index));
                            view.setImageBitmap(img);
                            Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(index < paths.size()-1) {
                            Bitmap img = BitmapFactory.decodeFile(paths.get(++index));
                            view.setImageBitmap(img);
                            Toast.makeText(this, "right2left swipe", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
