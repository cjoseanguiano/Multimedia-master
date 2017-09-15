package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.ArrayList;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
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
    private static final String CURRENT_VIDEO_FILE_PATH = "current_video_file_path";
    private static final String CURRENT_PHOTO_FILE_PATH = "current_photo_file_path";
    //    static final Integer VIDEO_CAMERA = 0x2;
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
    private Uri photoURI;
    private int checkTypePermission;
    private File photoFile;
    private File videoFile;

    @Override
//      protected void onCreate(@Nullable Bundle savedInstanceState) {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list_multimedia);
        Log.i(TAG, "onCreate: ");
        initInstances();
        label_Photo = getResources().getString(R.string.image);
        label_Video = getResources().getString(R.string.video);

        if (savedInstanceState != null) {
            String videoFilePath = savedInstanceState.getString(CURRENT_VIDEO_FILE_PATH);
            if (videoFilePath != null) {
                videoFile = new File(videoFilePath);
            }
            String photoFilePath = savedInstanceState.getString(CURRENT_PHOTO_FILE_PATH);
            if (photoFilePath != null) {
                photoFile = new File(photoFilePath);
            }
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    floatingActionButton.hide();
                    checkTypePermission = 1;
                    askForPermission(Manifest.permission.CAMERA, REQUEST_TAKE_VIDEO);

                } else {
                    floatingActionButton.hide();
                    checkTypePermission = 0;
                    askForPermission(Manifest.permission.CAMERA, REQUEST_TAKE_PHOTO);

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

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            switch (checkTypePermission) {
                case 0:
                    Log.i(TAG, "askForPermission: dispatchTakePictureIntent");
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    Log.i(TAG, "askForPermission: dispatchTakeVideoIntent");
                    try {
                        dispatchTakeVideoIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    Log.i(TAG, "askForPermission: default");
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: ");
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        dispatchTakeVideoIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

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
//        adapter.addFragment(new PhotoAlbumFragment(backPressed), label_Photo);
//        adapter.addFragment(new VideoAlbumFragment(backPressed), label_Video);

        adapter.addFragment(PhotoAlbumFragment.newInstance(backPressed), label_Photo);
        adapter.addFragment(VideoAlbumFragment.newInstance(backPressed), label_Video);
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


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoFile != null) {
            outState.putString(CURRENT_VIDEO_FILE_PATH, videoFile.getAbsolutePath());
        }
        if (photoFile != null) {
            outState.putString(CURRENT_PHOTO_FILE_PATH, photoFile.getAbsolutePath());
        }
    }

    private void dispatchTakeVideoIntent() throws IOException {
        Log.i(TAG, "dispatchTakeVideoIntent: ");
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                videoFile = MultimediaUtilities.createVideoFile();
            } catch (IOException ex) {
                return;
            }
            if (videoFile != null) {
                photoURI = Uri.fromFile(MultimediaUtilities.createVideoFile());
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
            }
        }

    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = MultimediaUtilities.createImageFile();
            } catch (IOException ex) {
                return;
            }
            if (photoFile != null) {
                takePictureIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                photoURI = Uri.fromFile(MultimediaUtilities.createImageFile());
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            Intent intent = new Intent(this, ShowMediaFileActivity.class);
            intent.putExtra(CAPTURE_PHOTO, photoFile.getAbsolutePath());
            startActivity(intent);
            this.finish();

            MediaScannerConnection.scanFile(MainAlbumListActivity.this,
                    new String[]{photoFile.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });


/*            Intent i = new Intent(this, ShowMediaFileActivity.class);
            i.putExtra(CAPTURE_PHOTO, photoFile.getAbsolutePath());
            startActivityForResult(i, 10);


//                Uri imageUri = Uri.parse(photoURI.toString());
//                Intent intent = new Intent(this, ShowMediaFileActivity.class);
//                intent.putExtra(CAPTURE_PHOTO, imageUri.getPath());
//                startActivity(intent);
//                this.finish();
*/

        }
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: ");
            Uri imageUri = Uri.parse(photoURI.toString());
            Intent videoPlayer = new Intent(this, VideoPlayerActivity.class);
            videoPlayer.putExtra(CAPTURE_VIDEO, imageUri.getPath());
            startActivityForResult(videoPlayer, EXTRA_TAKE_VIDEO);
        }
        if (requestCode == EXTRA_TAKE_VIDEO && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(photoURI.toString());
            ArrayList<String> pathVideo = new ArrayList<>();
            if (imageUri != null) {
                pathVideo.add(imageUri.getPath());
            }
            Intent intent = new Intent(this, ShowMediaFileActivity.class);
            intent.putExtra(ShowMediaFileActivity.EXTRA_RESULT_SELECTED_VIDEO, pathVideo);
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