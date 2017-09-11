package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Fragment.TypeAlbumFragment;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter.ViewPagerAdapter;

import java.util.ArrayList;

public class MainSingleAlbumActivity extends AppCompatActivity implements TypeAlbumFragment.OnMediaSingleAlbumListener {

    private static final String TAG = MainSingleAlbumActivity.class.getSimpleName();
    private static final String EXTRA_BUCKET = "extra_bucket";
    private static final String EXTRA_TYPE_ALBUM = "extra_type_album";
    public static final String EXTRA_TYPE_BUCKET = "extra_type_bucket";
    public static final String EXTRA_TYPE_FILE = "extra_type_file";
    public static final String EXTRA_RESULT_SELECTED_PICTURE = "extra_result_selected_picture";
    public static final String EXTRA_RESULT_SELECTED_VIDEO = "extra_result_selected_video";

    private ArrayList<String> path_On = new ArrayList<>();
    private String bucketPhotoViewer;
    private String typeOfSelect;
    private String typeFile;
    private String typeBucket;
    private String valueTypeBucket;
    private String valueTypeAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_album_multimedia);
        Log.i(TAG, "onCreate: ");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        path_On = bundle.getStringArrayList(EXTRA_RESULT_SELECTED_PICTURE);
        typeOfSelect = getIntent().getExtras().getString(EXTRA_TYPE_ALBUM);
        typeBucket = getIntent().getExtras().getString(EXTRA_BUCKET);
        bucketPhotoViewer = getIntent().getStringExtra(EXTRA_TYPE_BUCKET);
        typeFile = getIntent().getStringExtra(EXTRA_TYPE_FILE);

        toolbar.setTitle(typeBucket);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        checkValueofBucket(typeBucket, bucketPhotoViewer);
        checkValueofAlbum(typeFile, typeOfSelect);
        adapter.addFragment(new TypeAlbumFragment(valueTypeBucket, valueTypeAlbum, path_On), null);
        viewPager.setAdapter(adapter);
    }

    private void checkValueofAlbum(String typeFile, String typeOfSelect) {
        if (typeFile != null) {
            valueTypeAlbum = typeFile;
        } else {
            valueTypeAlbum = typeOfSelect;
        }
    }

    private void checkValueofBucket(String typeBucket, String bucketPhotoViewer) {
        if (typeBucket != null) {
            valueTypeBucket = typeBucket;
        } else {
            valueTypeBucket = bucketPhotoViewer;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMediaSelected(ArrayList<String> mediaList, String typeAlbum, String typeFile, boolean v) {
        Intent mIntent = new Intent();
        if (mediaList != null) {
            Log.i(TAG, "onMediaSelected: ");
            if (v) {
                mIntent.putStringArrayListExtra(String.valueOf(EXTRA_RESULT_SELECTED_PICTURE), mediaList);
            } else {
                mIntent.putStringArrayListExtra(String.valueOf(EXTRA_RESULT_SELECTED_VIDEO), mediaList);
            }
            mIntent.putExtra(EXTRA_TYPE_BUCKET, "" + typeAlbum);
            mIntent.putExtra(EXTRA_TYPE_FILE, "" + typeFile);
            setResult(RESULT_OK, mIntent);
            finish();
        }
    }
}
