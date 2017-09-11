package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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

    public static final String EXTRA_BUCKET = "extra_bucket";
    public static final String EXTRA_TYPE_ALBUM = "extra_type_album";
    private static final String BACK_PRESSED = "back_pressed";
    private String body;
    private boolean backPressed = false;
    public static final String EXTRA_BACK_SELECT = "extra_back_select";
    private String label_Photo;
    private String label_Video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list_multimedia);
        Log.i(TAG, "onCreate: ");
        label_Photo = getResources().getString(R.string.image);
        label_Video = getResources().getString(R.string.video);

        if (getIntent().hasExtra(EXTRA_NAME) || getIntent().hasExtra(EXTRA_JID)) {
            String data = getIntent().getExtras().getString(EXTRA_NAME);
            String dataJid = getIntent().getExtras().getString(EXTRA_JID);

            if (data != null && data.trim().length() > 0) {
                String value = ApplicationSingleton.getInstance().getString(R.string.titleMultimedia);
                body = String.format(value, data);
            } else {
                body = MultimediaUtilities.cutString(dataJid,"@");
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(body);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
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

}