package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.DataIntent;
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.MultimediaUtilities;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    public static final int RESULT_LOAD_IMAGE_MULTIMEDIA = 80;
    public static final int RESULT_MAIN_SINGLE_ALBUM_MULTIMEDIA = 81;
    public static final int RESULT_IMAGE_SELECTED_MULTIMEDIA = 82;
    public static final int RESULT_VIDEO_SELECTED_MULTIMEDIA = 83;
    public static final int RESULT_VIDEO_SELECTED = 84;

    private Button button;
    private String jid = "carchat@bsdenterprise.com";
    private String user = "Carlos Anguiano";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_multimedia);

        button = findViewById(R.id.buttonHome);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                MainAlbumListActivity.startForResult(HomeActivity.this, user, jid, RESULT_LOAD_IMAGE_MULTIMEDIA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_MULTIMEDIA && resultCode == Activity.RESULT_OK) {
            DataIntent.resultLoadImageMultimedia(HomeActivity.this, data);
        }

        if (requestCode == RESULT_MAIN_SINGLE_ALBUM_MULTIMEDIA && resultCode == Activity.RESULT_OK) {
            DataIntent.resultMainSingleAlbumMultimedia(HomeActivity.this, user, data);
        }

        if (requestCode == RESULT_VIDEO_SELECTED_MULTIMEDIA && resultCode == Activity.RESULT_OK) {
            DataIntent.resultVideoSelectedMultimedia(HomeActivity.this, data);
        }
        if (requestCode == RESULT_IMAGE_SELECTED_MULTIMEDIA && resultCode == Activity.RESULT_OK) {
            ArrayList<String> paths = MultimediaUtilities.selectedPhoto(data);
            if (paths != null) {
                // TODO: 15/09/17 Add method to send photos
                Toast.makeText(this, paths.size() + " Images uploaded", Toast.LENGTH_SHORT).show();
//            mPresenter.sendPhotoMessage(paths, data.getExtras().getString("photoDescription"));
            }
        }
        if (requestCode == RESULT_VIDEO_SELECTED && resultCode == Activity.RESULT_OK) {
            ArrayList<String> paths = MultimediaUtilities.selectedPhoto(data);
            if (paths != null) {
                // TODO: 15/09/17 Add method to send video
                String fileString = MultimediaUtilities.StringBuilder(paths);
                File file = new File(fileString);
                Toast.makeText(this, paths.size() + " Video uploaded " + file, Toast.LENGTH_SHORT).show();
//              mPresenter.sendVideoMessage(file);
            }
        }
        if (resultCode == RESULT_CANCELED) {
            if (requestCode == RESULT_MAIN_SINGLE_ALBUM_MULTIMEDIA ||
                    requestCode == RESULT_VIDEO_SELECTED_MULTIMEDIA ||
                    requestCode == RESULT_IMAGE_SELECTED_MULTIMEDIA) {
                DataIntent.resultCancelSingleMultimedia(HomeActivity.this);
            }
            if (requestCode == RESULT_LOAD_IMAGE_MULTIMEDIA) {
                DataIntent.resultCancelLoadMultimedia();
            }
        }
    }
}
