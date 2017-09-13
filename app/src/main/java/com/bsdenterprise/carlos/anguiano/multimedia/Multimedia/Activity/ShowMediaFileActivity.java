package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter.ShowMediaAdapter;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ShowMediaFileActivity extends AppCompatActivity {

    public static final String TAG = ShowMediaFileActivity.class.getSimpleName();
    public static final String EXTRA_RESULT_SELECTED_PICTURE = "extra_result_selected_picture";
    public static final String CAPTURE_PHOTO = "capture_photo";
    public static final String CAPTURE_VIDEO = "capture_video";
    public static final String EXTRA_RESULT_SELECTED_VIDEO = "extra_result_selected_video";
    private static final String EXTRA_TYPE_BUCKET = "extra_type_bucket";
    private static final String EXTRA_TYPE_FILE = "extra_type_file";

    private EditText photoDescription;
    private ViewPager viewPager;
    private ShowMediaAdapter adapter;
    private ArrayList<String> mImagePath = new ArrayList<>();
    private ImageView imageView;
    private boolean iconRemove = false;
    private String newPath;
    private ImageView addPicture;
    private Bundle bundle;
    private String typeBucket;
    private String typeFile;
    private int currentPage;
    private int position;
    private FloatingActionButton send;
    private BitmapFactory.Options options;
    private LinearLayout thumbnailsContainer;
    private Toolbar toolbar;
    private StringBuilder stringBuilder = new StringBuilder();
    private Uri mImagePathPhoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_multimedia);
        initInstances();
        setUpToolbar();
        showIntent();
//        inflateThumbnails(mImagePath);
        checkBundle(bundle);
        disableEditText(photoDescription);
        checkImage();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPhoto();

            }
        });

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPicture();
            }
        });

    }

    private void checkImage() {
        if (mImagePath != null) {
            for (int i = 0; i < mImagePath.size(); i++) {
                stringBuilder.append(mImagePath.get(i));
            }
            newPath = stringBuilder.toString();
            if (newPath.contains(".jpg") || (newPath.contains(".png")) || (newPath.contains(".jpeg"))) {
                createViewPager();
            }
        }
    }

    private void checkBundle(Bundle bundle) {
        if (bundle != null) {
            if (getIntent().hasExtra(EXTRA_RESULT_SELECTED_PICTURE)) {
                mImagePath = bundle.getStringArrayList(EXTRA_RESULT_SELECTED_PICTURE);
                inflateThumbnails(mImagePath);
            }
            if (getIntent().hasExtra(EXTRA_RESULT_SELECTED_VIDEO)) {
                iconRemove = true;
                addPicture.setVisibility(View.INVISIBLE);
                mImagePath = bundle.getStringArrayList(EXTRA_RESULT_SELECTED_VIDEO);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mImagePath.size(); i++) {
                    stringBuilder.append(mImagePath.get(i));
                }
                newPath = stringBuilder.toString();
                Bitmap bmThumbnail = ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(newPath,
                        MediaStore.Video.Thumbnails.MINI_KIND), 200, 200);
                createImageViewNew();
//                inflateThumbnails(mImagePath);
                imageView.setImageBitmap(bmThumbnail);
            }

            if (getIntent().hasExtra(CAPTURE_PHOTO)) {
                Log.i(TAG, "checkBundle: ");
                addPicture.setVisibility(View.GONE);
                iconRemove = true;
                mImagePathPhoto = Uri.parse(bundle.getString(CAPTURE_PHOTO));
                Uri imageUri = Uri.parse(mImagePathPhoto.toString());
                File file = new File(imageUri.getPath());
                try {
                    InputStream ims = new FileInputStream(file.getPath());
                    createImageViewNew();
                    imageView.setImageBitmap(BitmapFactory.decodeStream(ims));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            adapter = new ShowMediaAdapter(this, mImagePath);
        }
    }

    private void initInstances() {
        toolbar = findViewById(R.id.toolbar);
        thumbnailsContainer = findViewById(R.id.container);
        addPicture = findViewById(R.id.imageView5);
        send = findViewById(R.id.fab);
        photoDescription = findViewById(R.id.photoDescription);
        send = findViewById(R.id.fab);

    }

    private void showIntent() {
        bundle = getIntent().getExtras();
        mImagePath = getIntent().getExtras().getStringArrayList(EXTRA_RESULT_SELECTED_PICTURE);
        typeBucket = getIntent().getStringExtra(EXTRA_TYPE_BUCKET);
        typeFile = getIntent().getStringExtra(EXTRA_TYPE_FILE);
    }

    public void createImageViewNew() {
        FrameLayout frameLayoutV = findViewById(R.id.fragmentContainer);
        imageView = new ImageView(this);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
        imageView.setId(R.id.viewImageMultimedia);
        imageView.setLayoutParams(params);
        frameLayoutV.addView(imageView);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void sendPhoto() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("photoDescription", photoDescription.getText().toString());
        returnIntent.putStringArrayListExtra("paths", mImagePath);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void addPicture() {
        Intent intent = new Intent(this, MainSingleAlbumActivity.class);
        intent.putStringArrayListExtra(EXTRA_RESULT_SELECTED_PICTURE, mImagePath);
        intent.putExtra(EXTRA_TYPE_BUCKET, typeBucket);
        intent.putExtra(EXTRA_TYPE_FILE, typeFile);
        startActivityForResult(intent, 20);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && mImagePath.size() != 0) {

                try {
                    viewPager.setAdapter(null);
                    Uri resultUri = result.getUri();
                    String newPath = resultUri.getPath();
                    mImagePath.remove(position);
                    mImagePath.add(newPath);
                    adapter = new ShowMediaAdapter(this, mImagePath);
                    inflateThumbnails(mImagePath);
                    createViewPager();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.i(TAG, "onActivityResult: " + "" + result.getError());
            }
        }
        if (requestCode == 20 && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "onActivityResult: ");
            viewPager.setAdapter(null);
            mImagePath = data.getExtras().getStringArrayList(EXTRA_RESULT_SELECTED_PICTURE);
            inflateThumbnails(mImagePath);
            if (mImagePath != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mImagePath.size(); i++) {
                    stringBuilder.append(mImagePath.get(i));
                }
                String newString = stringBuilder.toString();
                if (newString.contains(".jpg") || (newString.contains(".png")) || (newString.contains(".jpeg"))) {
                    adapter = new ShowMediaAdapter(this, mImagePath);
                    viewPager.setAdapter(adapter);
                }
            }
        }
        if (requestCode == 20 && resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG, "onActivityResult: ");
            onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (!iconRemove) {
            menu.clear();
            MenuItem item = menu.add(Menu.NONE, R.id.crop_image_menu_crop, Menu.NONE, R.string.crop_image_menu_crop);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            item.setIcon(R.drawable.crop_multimedia);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.crop_image_menu_crop) {
            final int[] chatMessage = new int[1];
            chatMessage[0] = adapter.getItemPosition(currentPage);
            position = viewPager.getCurrentItem();

            String pos = adapter.getCurrentItem(position);
            CropImage.activity(Uri.fromFile(new File(pos)))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void cancelSend() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void createViewPager() {
        FrameLayout frameLayoutI = findViewById(R.id.fragmentContainer);
        viewPager = new ViewPager(this);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
        viewPager.setId(R.id.viewPagerMultimedia);
        viewPager.setLayoutParams(params);
        frameLayoutI.addView(viewPager);
        viewPager.setAdapter(adapter);
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setTextColor(Color.TRANSPARENT);
        editText.setHintTextColor(Color.TRANSPARENT);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void inflateThumbnails(ArrayList<String> mImagePath) {
        if (mImagePath != null && mImagePath.size() > 0) {
            for (int i = 0; i < this.mImagePath.size(); i++) {
                View imageLayout = getLayoutInflater().inflate(R.layout.item_thumbnails_multimedia, null);
                ImageView one = imageLayout.findViewById(R.id.img_thumb);
                one.setOnClickListener(onChagePageClickListener(i));
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                options.inDither = false;
                Uri uri = Uri.parse(this.mImagePath.get(i));
                Glide.with(this)
                        .load(new File(uri.getPath()))
                        .into(one);
                thumbnailsContainer.addView(imageLayout);
            }
        }
    }

    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(i);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        thumbnailsContainer.removeAllViews();
    }
}
