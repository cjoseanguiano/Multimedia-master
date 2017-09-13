package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter.ViewPagerAdapter;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Fragment.PhotoAlbumFragment;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Fragment.VideoAlbumFragment;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.ApplicationSingleton;
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.MultimediaUtilities;
import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Activity.VideoPlayerActivity;

import java.io.File;
import java.io.IOException;

import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.ShowMediaFileActivity.CAPTURE_PHOTO;
import static com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Activity.VideoPlayerActivity.CAPTURE_VIDEO;

public class MainAlbumListActivity extends AppCompatActivity implements PhotoAlbumFragment.OnMediaSelectedPhotoAlbum, VideoAlbumFragment.OnMediaSelectedVideoAlbum {
    public static final String TAG = MainAlbumListActivity.class.getSimpleName();

    public static final String EXTRA_NAME = "sendUser";//Displays the user Name
    public static final String EXTRA_JID = "extra_jid";//Displays the user Jid
    public static final String EXTRA_RESULT_SELECTED_ALBUM = "selected_media_album";//Send album result

    public static final String EXTRA_BUCKET = "extra_bucket";
    public static final String EXTRA_TYPE_ALBUM = "extra_type_album";
    private static final String BACK_PRESSED = "back_pressed";
    private static final int REQUEST_TAKE_PHOTO = 84;
    private static final int REQUEST_TAKE_VIDEO = 85;
    private static final int EXTRA_TAKE_VIDEO = 86;
    private static final int REQUEST_CAMERA = 0;
    private String body;
    private boolean backPressed = false;
    public static final String EXTRA_BACK_SELECT = "extra_back_select";
    private String label_Photo;
    private String label_Video;
    private FloatingActionButton fabAlbum;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    //    private String mCurrentPhotoPath;
    private Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list_multimedia);
        Log.i(TAG, "onCreate: ");
        initInstances();
        label_Photo = getResources().getString(R.string.image);
        label_Video = getResources().getString(R.string.video);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    Toast.makeText(MainAlbumListActivity.this, "Select Video", Toast.LENGTH_SHORT).show();
                    floatingActionButton.hide();
                    try {
                        dispatchTakeVideoIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainAlbumListActivity.this, "Select Camera", Toast.LENGTH_SHORT).show();
                    floatingActionButton.hide();
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        if (getIntent().hasExtra(EXTRA_NAME) || getIntent().hasExtra(EXTRA_JID)) {
            String data = getIntent().getExtras().getString(EXTRA_NAME);
            String dataJid = getIntent().getExtras().getString(EXTRA_JID);

            if (data != null && data.trim().length() > 0) {
                String value = ApplicationSingleton.getInstance().getString(R.string.titleMultimedia);
                body = String.format(value, data);
            } else {
                body = MultimediaUtilities.cutString(dataJid, "@");
            }
        }

        toolbar.setTitle(body);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabSelected: ");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabUnselected: ");
                if (tab.getPosition() == 1) {
                    floatingActionButton.setImageResource(R.drawable.ic_add_a_photo);
                    floatingActionButton.show();

                } else {
                    floatingActionButton.setImageResource(R.drawable.ic_videocam);
                    floatingActionButton.show();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabReselected: ");
            }
        });

    }

//    public void showCamera() {
//        Log.i(TAG, "Show camera button pressed. Checking permission.");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestCameraPermission();
//
//        } else {
//            Log.i(TAG, "CAMERA permission has already been granted. Displaying camera preview.");
//            try {
//                dispatchTakePictureIntent();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(viewPager, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainAlbumListActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }


    private void initInstances() {
        toolbar = findViewById(R.id.toolbar);
        floatingActionButton = findViewById(R.id.fabAlbum);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.i(TAG, "setupViewPager: ");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (getIntent().hasExtra(BACK_PRESSED)) {
            backPressed = true;
        }
        adapter.addFragment(new PhotoAlbumFragment(backPressed), label_Photo);
        adapter.addFragment(new VideoAlbumFragment(backPressed), label_Video);
        viewPager.setAdapter(adapter);
    }

    public static void startForResult(Activity activity, String name, String jid, int RESULT_CODE) {
        Log.i(TAG, "startForResult: ");
        Intent intent = new Intent(activity, MainAlbumListActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_JID, jid);
        activity.startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMediaSelected(String message, String type, boolean backPressed) {
        Log.i(TAG, "onMediaSelected: ");
        if (!backPressed) {
            Intent mIntent = new Intent();
            mIntent.putExtra(String.valueOf(EXTRA_RESULT_SELECTED_ALBUM), message);
            mIntent.putExtra(EXTRA_TYPE_ALBUM, "" + type);
            mIntent.putExtra(EXTRA_BUCKET, message);
            mIntent.putExtra(EXTRA_BACK_SELECT, false);
            setResult(RESULT_OK, mIntent);
            finish();
        } else {
            Intent mIntent = new Intent();
            mIntent.putExtra(String.valueOf(EXTRA_RESULT_SELECTED_ALBUM), message);
            mIntent.putExtra(EXTRA_TYPE_ALBUM, "" + type);
            mIntent.putExtra(EXTRA_BUCKET, message);
            mIntent.putExtra(EXTRA_BACK_SELECT, true);
            setResult(RESULT_OK, mIntent);
            finish();
        }

    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = MultimediaUtilities.createImageFile();
            } catch (IOException ex) {
                return;
            }
            if (photoFile != null) {
                photoURI = Uri.fromFile(MultimediaUtilities.createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private void dispatchTakeVideoIntent() throws IOException {
        Log.i(TAG, "dispatchTakeVideoIntent: ");
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = MultimediaUtilities.createVideoFile();
            } catch (IOException ex) {
                return;
            }
            if (photoFile != null) {
                photoURI = Uri.fromFile(MultimediaUtilities.createVideoFile());
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Uri imageUri = Uri.parse(photoURI.toString());
            Intent intent = new Intent(this, ShowMediaFileActivity.class);
            intent.putExtra(CAPTURE_PHOTO, imageUri.getPath());
            startActivity(intent);
            this.finish();

            MediaScannerConnection.scanFile(MainAlbumListActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: ");
            Uri imageUri = Uri.parse(photoURI.toString());
            Intent videoPlayer = new Intent(this, VideoPlayerActivity.class);
            videoPlayer.putExtra(CAPTURE_VIDEO, imageUri.getPath());
            startActivityForResult(videoPlayer, EXTRA_TAKE_VIDEO);

//            startActivity(videoPlayer);
//            this.finish();
        }
        if (requestCode == EXTRA_TAKE_VIDEO && resultCode == RESULT_OK){
            Uri imageUri = Uri.parse(photoURI.toString());
            Intent intent = new Intent(this, ShowMediaFileActivity.class);
            intent.putExtra(CAPTURE_PHOTO, imageUri.getPath());
            startActivity(intent);
            this.finish();

            MediaScannerConnection.scanFile(MainAlbumListActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
        if ((requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_TAKE_VIDEO) && resultCode == Activity.RESULT_CANCELED) {
            floatingActionButton.show();
        }
    }
}