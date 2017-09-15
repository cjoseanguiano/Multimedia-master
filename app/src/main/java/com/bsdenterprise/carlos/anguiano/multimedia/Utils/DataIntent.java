package com.bsdenterprise.carlos.anguiano.multimedia.Utils;

import android.content.Intent;
import android.util.Log;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.HomeActivity;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainAlbumListActivity;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainSingleAlbumActivity;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.ShowMediaFileActivity;
import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Activity.VideoPlayerActivity;

import java.util.ArrayList;

import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.HomeActivity.RESULT_IMAGE_SELECTED_MULTIMEDIA;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.HomeActivity.RESULT_LOAD_IMAGE_MULTIMEDIA;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.HomeActivity.RESULT_MAIN_SINGLE_ALBUM_MULTIMEDIA;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.HomeActivity.RESULT_VIDEO_SELECTED;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.HomeActivity.RESULT_VIDEO_SELECTED_MULTIMEDIA;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainAlbumListActivity.EXTRA_NAME;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainAlbumListActivity.EXTRA_RESULT_SELECTED_ALBUM;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainSingleAlbumActivity.EXTRA_RESULT_SELECTED_PICTURE;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainSingleAlbumActivity.EXTRA_RESULT_SELECTED_VIDEO;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainSingleAlbumActivity.EXTRA_TYPE_BUCKET;
import static com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainSingleAlbumActivity.EXTRA_TYPE_FILE;
import static com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Activity.VideoPlayerActivity.EXTRA_MEDIA_PATHS_VIDEO;

/**
 * Created by CarlosAnguiano on 15/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class DataIntent {

    private static final String TAG = DataIntent.class.getSimpleName();

    public static void resultLoadImageMultimedia(HomeActivity homeActivity, Intent data) {
        Log.i(TAG, "resultLoadImageMultimedia: ");
        if (data != null) {
            if (data.hasExtra(EXTRA_RESULT_SELECTED_ALBUM)) {
                Intent intent = new Intent(homeActivity, MainSingleAlbumActivity.class);
                intent.putExtra(MainAlbumListActivity.EXTRA_BUCKET, data.getStringExtra(MainAlbumListActivity.EXTRA_BUCKET));
                intent.putExtra(MainAlbumListActivity.EXTRA_TYPE_ALBUM, data.getStringExtra(MainAlbumListActivity.EXTRA_TYPE_ALBUM));
                intent.putExtra(MainAlbumListActivity.EXTRA_BACK_SELECT, data.getBooleanExtra(MainAlbumListActivity.EXTRA_BACK_SELECT, true));
                homeActivity.startActivityForResult(intent, RESULT_MAIN_SINGLE_ALBUM_MULTIMEDIA);
            }
        }
    }

    public static void resultMainSingleAlbumMultimedia(HomeActivity homeActivity, String user, Intent data) {
        Log.i(TAG, "resultMainSingleAlbumMultimedia: ");
        if (data != null) {
            if (data.hasExtra(EXTRA_RESULT_SELECTED_PICTURE)) {
                Intent i = new Intent(homeActivity, ShowMediaFileActivity.class);
                i.putExtra(ShowMediaFileActivity.EXTRA_RESULT_SELECTED_PICTURE, data.getStringArrayListExtra(EXTRA_RESULT_SELECTED_PICTURE));
                i.putExtra(EXTRA_TYPE_BUCKET, data.getStringExtra(EXTRA_TYPE_BUCKET));
                i.putExtra(EXTRA_TYPE_FILE, data.getStringExtra(EXTRA_TYPE_FILE));
                homeActivity.startActivityForResult(i, RESULT_IMAGE_SELECTED_MULTIMEDIA);
            }
            if (data.hasExtra(EXTRA_RESULT_SELECTED_VIDEO)) {
                Intent videoPlayer = new Intent(homeActivity, VideoPlayerActivity.class);
                videoPlayer.putExtra(EXTRA_MEDIA_PATHS_VIDEO, data.getStringArrayListExtra(EXTRA_RESULT_SELECTED_VIDEO));
                videoPlayer.putExtra(EXTRA_TYPE_BUCKET, data.getStringExtra(EXTRA_TYPE_BUCKET));
                videoPlayer.putExtra(EXTRA_TYPE_FILE, data.getStringExtra(EXTRA_TYPE_FILE));
                videoPlayer.putExtra(EXTRA_NAME, user);
                homeActivity.startActivityForResult(videoPlayer, RESULT_VIDEO_SELECTED_MULTIMEDIA);
            }
        }
    }

    public static void resultVideoSelectedMultimedia(HomeActivity homeActivity, Intent data) {
        Log.i(TAG, "resultVideoSelectedMultimedia: ");
        if (data != null) {
            if (data.hasExtra(EXTRA_MEDIA_PATHS_VIDEO)) {
                ArrayList<String> mImagePaths;
                mImagePaths = data.getExtras().getStringArrayList(EXTRA_MEDIA_PATHS_VIDEO);
                if (mImagePaths != null) {
                    Intent i = new Intent(homeActivity, ShowMediaFileActivity.class);
                    i.putExtra(ShowMediaFileActivity.EXTRA_RESULT_SELECTED_VIDEO, mImagePaths);
                    i.putExtra(EXTRA_TYPE_BUCKET, data.getStringExtra(EXTRA_TYPE_BUCKET));
                    i.putExtra(EXTRA_TYPE_FILE, data.getStringExtra(EXTRA_TYPE_FILE));
                    homeActivity.startActivityForResult(i, RESULT_VIDEO_SELECTED);
                }
            }
        }

    }

    public static void resultCancelSingleMultimedia(HomeActivity homeActivity) {
        Log.i(TAG, "resultCancelSingleMultimedia: ");
        Intent intent = new Intent(homeActivity, MainAlbumListActivity.class);
//        intent.putExtra(EXTRA_JID, jid);
//        intent.putExtra(EXTRA_NAME, user);
        homeActivity.startActivityForResult(intent, RESULT_LOAD_IMAGE_MULTIMEDIA);
    }

    public static void resultCancelLoadMultimedia() {
        Log.i(TAG, "resultCancelLoadMultimedia: ");
    }
}
