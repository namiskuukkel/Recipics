package software.kuukkel.fi.recipics.CreateRecipe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import software.kuukkel.fi.recipics.HelperClass;
import software.kuukkel.fi.recipics.Objects.ButtonAndFilepath;
import software.kuukkel.fi.recipics.R;

import static android.app.Activity.RESULT_OK;


public class CameraFragment extends Fragment {

    public final static String PATHS = "paths";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 42;
    View mView;
    LinearLayout parentLayout;

    ImageButton camera;
    private String mCurrentPhotoPath;
    private Uri mCurrentPhotoUri;
    private ArrayList<String> filePaths;
    private ArrayList<ButtonAndFilepath> deleteFileTuples;
    private PreserveFileUris mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (PreserveFileUris) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Always call the superclass first
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

        mView = inflater.inflate(R.layout.fragment_camera, container, false);

        //FileUri array is initialised when the parent activity is created. Hence, this works both
        //on the first time as well as the latter times
        filePaths = mCallback.getPicturePaths();

        parentLayout = (LinearLayout) mView.findViewById(R.id.camera_fragment);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        deleteFileTuples = new ArrayList<>();

        //If the array isn't empty, we are returning to this fragment and there is already a picture
        //to show
        if(filePaths.size() > 0 ) {
            for(final String path: filePaths) {
                if (path != null) {
                    RelativeLayout rl = new RelativeLayout(getActivity());
                    try {
                        ImageView mImageView = new ImageView(getActivity());
                        mImageView.setImageBitmap(
                                HelperClass.handleSamplingAndRotationBitmap(getActivity(),
                                        Uri.fromFile(new File(path))));
                        rl.addView(mImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ImageButton delete = new ImageButton(getActivity());
                    delete.setImageResource(R.drawable.delete);

                    ButtonAndFilepath tmp = new ButtonAndFilepath(delete, path);
                    deleteFileTuples.add(tmp);

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removeFile(view);
                        }
                    });

                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rl.addView(delete, params);
                    parentLayout.addView(rl);
                }
            }
        }

        camera = new ImageButton(getActivity());
        camera.setImageResource(R.drawable.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera(view);
            }});
        parentLayout.addView(camera, params);
        return mView;
    }

    private void startCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;

            //Check if the user has already accepted the permissions for memory accesses
            checkPermissions();

            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("st", ex.getMessage());
            }

            mCurrentPhotoUri = Uri.fromFile(photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    mCurrentPhotoUri);

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            filePaths.add(mCurrentPhotoUri.getPath());
            galleryAddPic();

            try {
                ImageView mImageView = new ImageView(getActivity());

                mImageView.setImageBitmap(
                        HelperClass.handleSamplingAndRotationBitmap(getActivity(), mCurrentPhotoUri));
                //Remove and re-add camera button to preserve the order of buttons
                parentLayout.removeView(camera);
                RelativeLayout rl = new RelativeLayout(getActivity());
                rl.addView(mImageView);
                ImageButton delete = new ImageButton(getActivity());
                delete.setImageResource(R.drawable.delete);

                ButtonAndFilepath tmp = new ButtonAndFilepath(delete, mCurrentPhotoUri.getPath());
                deleteFileTuples.add(tmp);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeFile(view);
                    }});

                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rl.addView(delete, p);
                delete.setLayoutParams(p);
                rl.setPadding(0, 0, 0, 20);
                parentLayout.addView(rl);
                parentLayout.addView(camera);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Debugging to see how lifecycle events act
    @Override
    public void onPause() {
        super.onPause();
        mCallback.savePicturePaths(filePaths);
        Log.d("pause", "onPause: Camera");
    }

    //Create a file where the picture to be taken will be saved
    //http://www.androidhive.info/2013/09/android-working-with-camera-api/
    private File createImageFile() throws IOException {

        String dirName = "Recipics";
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                dirName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(dirName, "Oops! Failed create "
                        + dirName + " directory");
                return null;
            }
        }
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                mediaStorageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    //Trigger the phone's gallery to show the new picture
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Log.d("File path", mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void removeFile(View view) {
        //Search the tuples for the button that was clicked
        for(int i = 0; i < deleteFileTuples.size(); i++) {
            if(deleteFileTuples.get(i).getDelete() == view) {
                //Search the filepaths for the path that was connected to the button
                for (int j = 0; j < filePaths.size(); j++) {
                    if (filePaths.get(j) == deleteFileTuples.get(i).getFilePath()) {
                        //Remove the button from both the filesystem and filepaths list
                        File file = new File(deleteFileTuples.get(i).getFilePath());
                        file.delete();
                        filePaths.remove(j);
                        break;
                    }
                }
            }
        }
        //Delete the layout containing the picture and the button from the top level layout
        RelativeLayout parent = (RelativeLayout) view.getParent();
        parentLayout.removeView(parent);
    }

    //For new versions of android: Ask for application permissions on runtime
    public void checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            }
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    //On the first run, permissions must be requested to access external storage
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //This method is for the parent activity to get the fileuris, when user presses the save button
    public ArrayList<String> getPicturePaths() {
        return filePaths;
    }

public static interface PreserveFileUris {
    public void savePicturePaths(ArrayList<String> paths);
    public ArrayList<String> getPicturePaths();
}
}
