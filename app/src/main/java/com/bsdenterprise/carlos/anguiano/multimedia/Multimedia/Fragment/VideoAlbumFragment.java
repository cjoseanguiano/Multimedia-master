package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter.MediaAdapterAllAlbum;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Interface.MediaAdapterAllAlbumClickListener;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model.DataPicturesAlbum;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Utils.GridRecyclerView;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class VideoAlbumFragment extends Fragment implements MediaAdapterAllAlbumClickListener {

    private static final String TAG = VideoAlbumFragment.class.getSimpleName();
    private static final String type = "video";
    private GridRecyclerView mRecyclerView;
    private MediaAdapterAllAlbum adapter;
    private OnMediaSelectedVideoAlbum mCallback;
    private View view = null;
    private boolean boolean_folder;
    private boolean backPressed = false;
    public static ArrayList<DataPicturesAlbum> modelimages = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public VideoAlbumFragment(boolean backPressed) {
        this.backPressed = backPressed;

    }

    public interface OnMediaSelectedVideoAlbum {
        void onMediaSelected(String message, String type, boolean backPressed);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnMediaSelectedVideoAlbum) context;
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
    public void itemClicked(DataPicturesAlbum d, String message, String type, boolean backPressed) {
        mCallback.onMediaSelected(message, type, backPressed);
    }

    private void parseAllVideo(String type) {
        try {
            int int_position = 0;
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            String absolutePathOfImage;

            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//            String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
            String[] projection = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DATA};
            String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            cursor = getActivity().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");


            if (cursor != null) {
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);


                while (cursor.moveToNext()) {

                    absolutePathOfImage = cursor.getString(column_index_data);
                    Log.i(TAG, "parseAllVideos: " + absolutePathOfImage);
                    Log.i(TAG, "parseAllVideos: " + cursor.getString(column_index_folder_name));

                    for (int i = 0; i < modelimages.size(); i++) {
                        if (modelimages.get(i).getFolder().equals(cursor.getString(column_index_folder_name))) {
                            boolean_folder = true;
                            int_position = i;
                            break;
                        } else {
                            boolean_folder = false;
                        }
                    }

                    if (boolean_folder) {
                        ArrayList<String> al_path = new ArrayList<>();
                        al_path.addAll(modelimages.get(int_position).getPathSize());
                        al_path.add(absolutePathOfImage);
                        modelimages.get(int_position).setPathSize(al_path);
                    } else {
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(absolutePathOfImage);
                        DataPicturesAlbum mediaFileInfo = new DataPicturesAlbum();
                        mediaFileInfo.setFolder(cursor.getString(column_index_folder_name));
                        mediaFileInfo.setPathSize(paths);
                        mediaFileInfo.setType(type);
                        modelimages.add(mediaFileInfo);
                    }
                }
                cursor.close();

                for (int i = 0; i < modelimages.size(); i++) {
                    Log.e("FOLDER", modelimages.get(i).getFolder());
                    for (int j = 0; j < modelimages.get(i).getPathSize().size(); j++) {
                        Log.e("FILE", modelimages.get(i).getPathSize().get(j));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_multimedia, container, false);
        mRecyclerView = view.findViewById(R.id.grid_recycler);
        new MediaAsyncTask().execute(type);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.subscribe(this);
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
            Integer result = 0;
            String type = params[0];
            try {
                modelimages = new ArrayList<>();
                if (type.equalsIgnoreCase("video")) {
                    parseAllVideo(type);
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
            if (result == 1) {

                adapter = new MediaAdapterAllAlbum(getActivity(), modelimages, backPressed);
                mRecyclerView.setAdapter(adapter);
                adapter.subscribe(VideoAlbumFragment.this);
                view.findViewById(R.id.xpxrogressBar).setVisibility(View.GONE);


            } else {
                Log.e(TAG, "Failed to show list");
            }
        }
    }
}

