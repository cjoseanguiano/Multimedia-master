package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Byte2;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.MultimediaUtilities;
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.ApplicationSingleton;
import com.bsdenterprise.carlos.anguiano.multimedia.R;

public class MainAlbumListActivity extends AppCompatActivity implements PhotoAlbumFragment.OnMediaSelectedPhotoAlbum, VideoAlbumFragment.OnMediaSelectedVideoAlbum {
    public static final String TAG = MainAlbumListActivity.class.getSimpleName();

    public static final String EXTRA_NAME = "sendUser";//Displays the user Name
    public static final String EXTRA_JID = "extra_jid";//Displays the user Jid
    public static final String EXTRA_RESULT_SELECTED_ALBUM = "selected_media_album";//Send album result
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final String EXTRA_BUCKET = "extra_bucket";
    public static final String EXTRA_TYPE_ALBUM = "extra_type_album";
    private static final String BACK_PRESSED = "back_pressed";
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
    private int mCurrentPagePosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list_multimedia);
        Log.i(TAG, "onCreate: ");
        startView();
        label_Photo = getResources().getString(R.string.image);
        label_Video = getResources().getString(R.string.video);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    Toast.makeText(MainAlbumListActivity.this, "Select A", Toast.LENGTH_SHORT).show();
                    floatingActionButton.hide();

                } else {
                    Toast.makeText(MainAlbumListActivity.this, "Select B", Toast.LENGTH_SHORT).show();
                    floatingActionButton.hide();
                    dispatchTakePictureIntent();
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

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "onPageScrolled: ");
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i(TAG, "onPageSelected: ");
//                if (position == 0) {
//                    floatingActionButton.setImageResource(R.drawable.ic_add_a_photo);
//                } else {
//                    floatingActionButton.setImageResource(R.drawable.ic_videocam);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.i(TAG, "onPageScrollStateChanged: ");
//            }
//        });


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

    private void startView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabAlbum);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

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


    private void dispatchTakePictureIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}