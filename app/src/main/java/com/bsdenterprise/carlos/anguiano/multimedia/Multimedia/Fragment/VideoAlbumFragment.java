package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter.MediaAdapterAllAlbum;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Interface.MediaAdapterAllAlbumClickListener;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model.DataPicturesAlbum;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Utils.GridRecyclerView;
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.MultimediaUtilities;

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

//    @SuppressLint("ValidFragment")
//    public VideoAlbumFragment(boolean backPressed) {
//        this.backPressed = backPressed;
//
//    }
    public static VideoAlbumFragment newInstance(boolean backPressed) {
        VideoAlbumFragment f = new VideoAlbumFragment();
        Bundle args = new Bundle();
        args.putBoolean("check", backPressed);
        f.setArguments(args);
        return f;
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
                    + " must implement VideoAlbumFragment");
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
        MultimediaUtilities.parseAllVideo(type, (FragmentActivity) mCallback,boolean_folder,modelimages);
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

