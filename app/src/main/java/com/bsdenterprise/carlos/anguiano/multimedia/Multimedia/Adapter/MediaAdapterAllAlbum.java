package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Interface.MediaAdapterAllAlbumClickListener;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model.DataPicturesAlbum;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Carlos Anguiano on 05/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class MediaAdapterAllAlbum extends RecyclerView.Adapter<MediaAdapterAllAlbum.MediaListHolder> {
    private static final String TAG = MediaAdapterAllAlbum.class.getSimpleName();
    private List<DataPicturesAlbum> itemList;
    private MediaAdapterAllAlbumClickListener mListener;
    private static final int MAX_WIDTH = 150;
    private static final int MAX_HEIGHT = 150;
    private int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
    private boolean backPressed = false;
    private Activity context;

    public MediaAdapterAllAlbum(Activity context, List<DataPicturesAlbum> mediaList, boolean backPressed) {
        Log.i(TAG, "MediaAdapterAllAlbums: ");
        this.context = context;
        this.itemList = mediaList;
        this.backPressed = backPressed;
    }

    public void subscribe(MediaAdapterAllAlbumClickListener mListener) {
        this.mListener = mListener;
    }

    public void unsubscribe() {
        this.mListener = null;
    }

    @Override
    public MediaListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.all_album_multimedia, viewGroup, false);

        return new MediaListHolder(v);
    }

    @Override
    public void onBindViewHolder(MediaListHolder mediaListRowHolder, final int i) {
        mediaListRowHolder.bindTo(mediaListRowHolder, i);
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    class MediaListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView picture;
        TextView albumName;
        TextView nMedia;

        MediaListHolder(View view) {
            super(view);
            this.picture = view.findViewById(R.id.album_preview);
            this.albumName = view.findViewById(R.id.album_name);
            this.nMedia = view.findViewById(R.id.album_media_label);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DataPicturesAlbum message = itemList.get(getAdapterPosition());
            int pos = getAdapterPosition();
            if (mListener != null) {
                mListener.itemClicked(itemList.get(pos), message.getFolder(), message.getType(), backPressed);
            }

        }

        @SuppressLint("SetTextI18n")
        public void bindTo(MediaListHolder mediaListRowHolder, int i) {
            mediaListRowHolder.albumName.setText(itemList.get(i).getFolder());
            mediaListRowHolder.nMedia.setText(itemList.get(i).getPathSize().size() + "");

            try {
                String uriImage = "file://" + itemList.get(i).getPathSize().get(0);
                showGlide(uriImage, mediaListRowHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showGlide(String uriImage, MediaListHolder mediaListRowHolder) {
            RequestOptions options = new RequestOptions()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .centerCrop()
                    .error(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(context)
                    .load(uriImage)
                    .apply(options)
                    .into(mediaListRowHolder.picture);

        }
    }
}
