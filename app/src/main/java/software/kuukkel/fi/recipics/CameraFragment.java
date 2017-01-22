package software.kuukkel.fi.recipics;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class CameraFragment extends Fragment implements View.OnClickListener {

    public final static String PATHS = "paths";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 42;
    private String mCurrentPhotoPath;
    private Uri mCurrentPhotoUri;
    private ArrayList<Uri> fileUris;

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

        View mahView = inflater.inflate(R.layout.fragment_camera, container, false);
        mahView.findViewById(R.id.newPicture).setOnClickListener(this);

        //FileUri array is initialised when the parent activity is created. Hence, this works both
        //on the first time as well as the latter times
        fileUris = mCallback.getPictureUris();
        //If the array isn't empty, we are returning to this fragment and there is already a picture
        //to show
        if(fileUris.size() > 0 ) {
            ImageView mImageView = (ImageView) mahView.findViewById(R.id.capturedImageview);
            if (fileUris.get(0) != null ) {

                try {
                    mImageView.setImageBitmap(
                            HelperClass.handleSamplingAndRotationBitmap(getActivity(), fileUris.get(0)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }



        return mahView;
    }

    public void onClick(View view) {
        if(view.getId() == R.id.newPicture) {
            startCamera(view);
        }
    }

    private void startCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            //Check if the user has already accepted the permissions for memory accesses
            checkPermissions();
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("st", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                mCurrentPhotoUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        mCurrentPhotoUri);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            fileUris.add(mCurrentPhotoUri);
            galleryAddPic();

            //TODO: Vertical pictures won't show vertical. Whyyyy?!
            try {
                ImageView mImageView = (ImageView) getView().findViewById(R.id.capturedImageview);

                mImageView.setImageBitmap(
                        HelperClass.handleSamplingAndRotationBitmap(getActivity(), mCurrentPhotoUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Debugging to see how lifecycle events act
    @Override
    public void onPause() {
        super.onPause();
        mCallback.savePictureUris(fileUris);
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
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
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
    public ArrayList<Uri> getPictureUris() {
        return fileUris;
    }

    public static interface PreserveFileUris {
        public void savePictureUris(ArrayList<Uri> path);
        public ArrayList<Uri> getPictureUris();
    }
}
