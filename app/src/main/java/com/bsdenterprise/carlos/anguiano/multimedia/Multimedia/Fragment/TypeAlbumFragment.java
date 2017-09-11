package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Activity.MainAlbumListActivity;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Interface.MediaSingleClicked;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model.DataPicture;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter.MediaAdapterSingleAlbum;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Utils.GridRecyclerView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@SuppressLint("ValidFragment")
public class TypeAlbumFragment extends Fragment implements MediaSingleClicked, MediaAdapterSingleAlbum.SendMessageActionsListener {

    private static final String TAG = TypeAlbumFragment.class.getSimpleName();
    private List<DataPicture> mediaList = new ArrayList<>();
    private GridRecyclerView mRecyclerView;
    private MediaAdapterSingleAlbum adapter;
    private Cursor cursor;
    private OnMediaSingleAlbumListener mCallback;
    private ArrayList<String> path_On = new ArrayList<>();
    private View view = null;
    private String typeBucket;
    private List<DataPicture> listFilterBucket = new ArrayList<>();
    private static String type = "";
    private MultiSelector mMultiSelector = new MultiSelector();

    public interface OnMediaSingleAlbumListener {
        void onMediaSelected(ArrayList<String> mediaList, String message, String typeAlbum, boolean v);
    }

    @SuppressLint("ValidFragment")
    public TypeAlbumFragment(String valueTypeBucket, String valueTypeAlbum, ArrayList<String> path_On) {
        this.typeBucket = valueTypeBucket;
        this.path_On = path_On;
        if (valueTypeAlbum.contains("images")) {
            type = "images";
        } else {
            type = "video";
        }
    }

    @Override
    public void forwardMessage(ArrayList<String> dataPictures, String typeBuckets, String typeAlbum, boolean v) {
        Log.e(TAG, "ForwardMessage");
        ArrayList<String> selectedPictures = new ArrayList<>();
        for (int i = 0; i < dataPictures.size(); i++) {
            selectedPictures.add(dataPictures.get(i));
            System.out.println("Values " + dataPictures.get(i));
        }
        mCallback.onMediaSelected(selectedPictures, typeBuckets, typeAlbum, v);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnMediaSingleAlbumListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ProgressBar");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void itemClicked(DataPicture dataPictures, String typeAlbum, String typeFile, boolean v) {
        ArrayList<String> selectedPictures = new ArrayList<>();
        selectedPictures.add(dataPictures.getFilePath());
        mCallback.onMediaSelected(selectedPictures, typeAlbum, typeFile, v);

    }

    private void parseAllData(String type) {
        try {
            switch (type) {
                case "images":
                    parseImages();
                    break;
                case "video":
                    parseVideo();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseImages() {
        Log.i(TAG, "parseAllImages: Images");
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        cursor = getActivity().getContentResolver().query(images,
                projection,
                null,
                null,
                null
        );
        int size = 0;
        if (cursor != null) {
            size = cursor.getCount();
        }
        if (size == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            String bucket;
            String path;
            int folder_Image = cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int path_Local = cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media.DATA);


            do {
                DataPicture mediaFileInfo = new DataPicture();
                path = cursor.getString(path_Local);
                bucket = cursor.getString(folder_Image);
                mediaFileInfo.setFilePath(path);
                mediaFileInfo.setFileType(type);
                mediaFileInfo.setBucket(bucket);
                mediaList.add(mediaFileInfo);
                if (typeBucket != null) {
                    if (mediaFileInfo.getBucket().contains(typeBucket)) {
                        listFilterBucket.add(mediaFileInfo);
                    }
                }
            } while (cursor.moveToNext());
        }
    }

    private void parseVideo() {
        Log.i(TAG, "parseAllVideo: Video");
        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.DATA
        };

        Uri images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        cursor = getActivity().getContentResolver().query(images,
                projection,
                null,
                null,
                null
        );
        int size = 0;
        if (cursor != null) {
            size = cursor.getCount();
        }
        if (size == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            String bucket;
            String path;
            int folder_Image = cursor.getColumnIndexOrThrow(
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

            int path_Local = cursor.getColumnIndexOrThrow(
                    MediaStore.Video.Media.DATA);


            do {
                DataPicture mediaFileInfo = new DataPicture();
                path = cursor.getString(path_Local);
                bucket = cursor.getString(folder_Image);
                mediaFileInfo.setFilePath(path);
                mediaFileInfo.setFileType(type);
                mediaFileInfo.setBucket(bucket);
                mediaList.add(mediaFileInfo);
                if (typeBucket != null) {
                    if (mediaFileInfo.getBucket().contains(typeBucket)) {
                        listFilterBucket.add(mediaFileInfo);
                    }
                }
            } while (cursor.moveToNext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_type_multimedia, container, false);
        mRecyclerView = view.findViewById(R.id.grid_recycler);
        new MediaAsyncTask().execute(type);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.subscribe(this);
            adapter.setOnMessageActionsListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(getContext(), MainAlbumListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.unsubscribe();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MediaAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            SystemClock.sleep(500);

            Integer result;
            String type = params[0];
            try {
                mediaList = new ArrayList<>();
                if (type.equalsIgnoreCase("images")) {
                    parseAllData(type);
                    result = 1;
                } else {
                    parseAllData(type);
                    result = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            view.findViewById(R.id.xpxrogressBar).setVisibility(View.GONE);
            if (result == 1) {
                adapter = new MediaAdapterSingleAlbum(getActivity(), path_On, typeBucket, listFilterBucket, type, mMultiSelector);
                mRecyclerView.setAdapter(adapter);
                adapter.subscribe(TypeAlbumFragment.this);
                adapter.setOnMessageActionsListener(TypeAlbumFragment.this);
                typeBucket = null;
                type = null;

            }
        }
    }
}