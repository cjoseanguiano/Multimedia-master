package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Interface.MediaSingleClicked;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model.DataPicture;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.Utils.MultimediaUtilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Anguiano on 05/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class MediaAdapterSingleAlbum extends RecyclerView.Adapter<MediaAdapterSingleAlbum.MediaListHolder> {
    public static final String TAG = MediaAdapterSingleAlbum.class.getSimpleName();
    private static final int MAX_WIDTH = 150;
    private static final int MAX_HEIGHT = 150;
    private int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
    private SendMessageActionsListener messageActionsListener;
    private MediaSingleClicked mListener;
    private List<DataPicture> itemList;
    private ArrayList<DataPicture> selectedMessages = new ArrayList<>();
    private ArrayList<String> pathSelected = new ArrayList<>();
    private boolean activateModeDisplay = false;
    private Activity context;
    private ActionMode customMode;
    private String typeBuckets;
    private String typeAlbum;
    private MultiSelector mMultiSelector;
    private boolean onLongClicks = false;
    private int contador;
    float screenWidth;
    float newHeight;
    private final String file = "file://";

    public MediaAdapterSingleAlbum(Activity context, ArrayList<String> path_On, String typeBucket,
                                   List<DataPicture> listFilterBucket, String typeAlbum, MultiSelector mMultiSelector) {
        this.context = context;
        this.pathSelected = path_On;
        this.typeBuckets = typeBucket;
        this.itemList = listFilterBucket;
        this.typeAlbum = typeAlbum;
        this.mMultiSelector = mMultiSelector;
    }

    public void onDestroy() {
        removeListeners();
    }

    public void setOnMessageActionsListener(SendMessageActionsListener l) {
        messageActionsListener = l;
    }

    private void removeListeners() {
        messageActionsListener = null;
    }

    public void subscribe(MediaSingleClicked mListener) {
        this.mListener = mListener;
    }

    public void unsubscribe() {
        this.mListener = null;
    }

    public interface SendMessageActionsListener {
        void forwardMessage(ArrayList<String> idMessages, String typeBuckets, String typeAlbum, boolean v);
    }

    @Override
    public MediaListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.single_album_multimedia, viewGroup, false);
        return new MediaListHolder(v);
    }

    @Override
    public void onBindViewHolder(MediaListHolder mediaListRowHolder, final int i) {
        screenWidth = getScreenWidth(context);
        newHeight = screenWidth;
        mediaListRowHolder.bindTo(mediaListRowHolder, i);
    }

    float getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return (float) outMetrics.widthPixels;
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    class MediaListHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageView;

        MediaListHolder(View view) {
            super(view, mMultiSelector);
            mMultiSelector.setSelectable(true);
            this.imageView = view.findViewById(R.id.photo_preview);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DataPicture messages = itemList.get(getAdapterPosition());
            AppCompatActivity activity = (AppCompatActivity) context;

            if (selectedMessages != null) {
                contador = selectedMessages.size();
            }
            if (mMultiSelector.isSelectable()) {
                mMultiSelector.tapSelection(this);
                if (messages.getFileType().contains("images")) {
                    if (v.isSelected()) {
                        contador = contador - 1;
                        addMessage(messages);
                        itemView.setSelected(!itemView.isSelected());
                        if (customMode == null) {
                            activity.startSupportActionMode(customModeCallback);
                            customMode.setTitle("Seleccionado " + contador);

                        } else {
                            customMode.setTitle("Seleccionado " + contador);
                        }
                    } else {
                        contador = contador + 1;
                        if (pathSelected != null && pathSelected.size() > 0) {
                            addMessage(messages);
                            if (!activateModeDisplay) {
                                if (v.isSelected()) {
                                } else {
                                    itemView.setSelected(!itemView.isSelected());
                                }
                                if (customMode == null) {
                                    activity.startSupportActionMode(customModeCallback);
                                    customMode.setTitle("Seleccionado " + contador);

                                } else {
                                    customMode.setTitle("Seleccionado " + contador);
                                }
                            } else {
                                mListener.itemClicked(messages, typeBuckets, typeAlbum, true);
                            }

                        } else {
                            if (onLongClicks) {
                                addMessage(messages);
                                if (customMode == null) {
                                    activity.startSupportActionMode(customModeCallback);
                                    customMode.setTitle("Seleccionado " + contador);

                                } else {
                                    customMode.setTitle("Seleccionado " + contador);
                                }
                            } else {
                                mListener.itemClicked(messages, typeBuckets, typeAlbum, true);
                            }
                        }
                    }
                } else {
                    mListener.itemClicked(messages, typeBuckets, typeAlbum, false);
                }

            }
        }

        @Override
        public boolean onLongClick(View v) {
            DataPicture message = itemList.get(getAdapterPosition());
            AppCompatActivity activity = (AppCompatActivity) context;
            onLongClicks = true;
            if (selectedMessages != null) {
                contador = selectedMessages.size();
            }

            if (mMultiSelector.isSelectable()) {
                mMultiSelector.tapSelection(this);
                if (message.getFileType().contains("images")) {
                    addMessage(message);
                    if (v.isSelected()) {
                        contador = contador - 1;
                        itemView.setSelected(!itemView.isSelected());
                        if (customMode == null) {
                            activity.startSupportActionMode(customModeCallback);
                            customMode.setTitle("Seleccionado " + contador);

                        } else {
                            customMode.setTitle("Seleccionado " + contador);
                        }
                    } else {
                        contador = contador + 1;
                        itemView.setSelected(!itemView.isSelected());
                        if (customMode == null) {
                            activity.startSupportActionMode(customModeCallback);
                            customMode.setTitle("Seleccionado " + contador);
                        } else {
                            customMode.setTitle("Seleccionado " + contador);
                        }
                    }
                }
            }
            return true;
        }

        public void bindTo(MediaListHolder mediaListRowHolder, int i) {
            DataPicture message = itemList.get(i);
            String uriImage = file + message.getFilePath();

            try {
                if (message.getFileType().equalsIgnoreCase("video")) {
                    MultimediaUtilities.showGlide(uriImage, context, mediaListRowHolder.imageView);
                } else {
                    if (pathSelected != null) {
                        for (String pathSelectedFile : pathSelected) {
                            if (message.getFilePath().equals(pathSelectedFile)) {
                                itemView.setSelected(!itemView.isSelected());
                                if (!selectedMessages.contains(message)) {
                                    mMultiSelector.tapSelection(this);
                                    if (pathSelected != null && pathSelected.size() > 0) {
                                        Log.i(TAG, "bindTo: ");
                                        selectedMessages.add(message);
                                    }
                                }
                            }
                        }

                    }
                    Log.i(TAG, "bindTo: " + uriImage);
                    MultimediaUtilities.showGlide(uriImage, context, mediaListRowHolder.imageView);
                }
                Log.i(TAG, "bindTo: ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private ActionMode.Callback customModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuItem item;
            customMode = mode;
            item = menu.add(Menu.NONE, R.id.forward_multimedia, Menu.NONE, R.string.OK);
            item.setTitle("OK");
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.forward_multimedia) {
                if (messageActionsListener != null) {
                    ArrayList<String> messagesIds = new ArrayList<>();
                    for (DataPicture listSelected : selectedMessages) {
                        if (listSelected != null) {
                            messagesIds.add(listSelected.getFilePath());
                        }
                    }
                    if (selectedMessages.size() > 0) {
                        messageActionsListener.forwardMessage(messagesIds, typeBuckets, typeAlbum, true);
                    } else {
                        activateModeDisplay = true;
                    }
                }
                mode.finish();
                return true;
            } else {
                return false;
            }
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            notifyDataSetChanged();
            selectedMessages.clear();
            mMultiSelector.clearSelections();
            onLongClicks = false;
            customMode.finish();
            if (pathSelected != null) {
                pathSelected.clear();
            }
        }
    };

    private void addMessage(DataPicture message) {
        MultimediaUtilities.addMessage(message,selectedMessages);
    }

}