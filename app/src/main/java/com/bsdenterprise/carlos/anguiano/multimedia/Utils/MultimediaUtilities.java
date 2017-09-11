package com.bsdenterprise.carlos.anguiano.multimedia.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model.DataPicture;
import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model.DataPicturesAlbum;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Carlos Anguiano on 11/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class MultimediaUtilities {

    private static String TAG = MultimediaUtilities.class.getSimpleName();

    public static String cutString(String dataJid, String cutValue) {
        Log.i(TAG, "cutString: ");
        String[] parts = dataJid.split(cutValue);
        String user = parts[0];
        String value = ApplicationSingleton.getInstance().getString(R.string.titleMultimedia);
        String body = String.format(value, user);
        return body;
    }


    public static ArrayList<DataPicturesAlbum> parseAllImages(String type,
                                                              FragmentActivity activity,
                                                              boolean folder,
                                                              ArrayList<DataPicturesAlbum> modelImages) {
        Log.i(TAG, "parseAllImages: ");
        try {
            int int_position = 0;
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            String absolutePathOfImage;

            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

            String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");


            if (cursor != null) {
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);


                while (cursor.moveToNext()) {

                    absolutePathOfImage = cursor.getString(column_index_data);

                    for (int i = 0; i < modelImages.size(); i++) {
                        if (modelImages.get(i).getFolder().equals(cursor.getString(column_index_folder_name))) {
                            folder = true;
                            int_position = i;
                            break;
                        } else {
                            folder = false;
                        }
                    }
                    if (folder) {
                        ArrayList<String> al_path = new ArrayList<>();
                        al_path.addAll(modelImages.get(int_position).getPathSize());
                        al_path.add(absolutePathOfImage);
                        modelImages.get(int_position).setPathSize(al_path);
                    } else {
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(absolutePathOfImage);
                        DataPicturesAlbum mediaFileInfo = new DataPicturesAlbum();
                        mediaFileInfo.setFolder(cursor.getString(column_index_folder_name));
                        mediaFileInfo.setPathSize(paths);
                        mediaFileInfo.setType(type);
                        modelImages.add(mediaFileInfo);
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelImages;
    }


    public static ArrayList<DataPicturesAlbum> parseAllVideo(String type,
                                                             FragmentActivity activity,
                                                             boolean folder,
                                                             ArrayList<DataPicturesAlbum> modelVideo) {
        Log.i(TAG, "parseAllVideo: ");
        try {
            int int_position = 0;
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            String absolutePathOfImage;

            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DATA};
            String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");


            if (cursor != null) {
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);


                while (cursor.moveToNext()) {

                    absolutePathOfImage = cursor.getString(column_index_data);
                    for (int i = 0; i < modelVideo.size(); i++) {
                        if (modelVideo.get(i).getFolder().equals(cursor.getString(column_index_folder_name))) {
                            folder = true;
                            int_position = i;
                            break;
                        } else {
                            folder = false;
                        }
                    }

                    if (folder) {
                        ArrayList<String> al_path = new ArrayList<>();
                        al_path.addAll(modelVideo.get(int_position).getPathSize());
                        al_path.add(absolutePathOfImage);
                        modelVideo.get(int_position).setPathSize(al_path);
                    } else {
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(absolutePathOfImage);
                        DataPicturesAlbum mediaFileInfo = new DataPicturesAlbum();
                        mediaFileInfo.setFolder(cursor.getString(column_index_folder_name));
                        mediaFileInfo.setPathSize(paths);
                        mediaFileInfo.setType(type);
                        modelVideo.add(mediaFileInfo);
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelVideo;
    }


    public static void showGlide(String uri, Activity context, ImageView picture) {
        Log.i(TAG, "showGlide: ");
        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .centerCrop()
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(options)
                .thumbnail(0.5f)
                .into(picture);
    }


    public Uri imageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void addMessage(DataPicture message, ArrayList<DataPicture> selectedMessages) {
        Log.i(TAG, "addMessage: ");

        if (message.getFileType().equalsIgnoreCase("video")) {
            Log.w("A", "if");
        }
        int i = 0;
        boolean exists = false;
        for (DataPicture dataPictures : selectedMessages) {
            if (dataPictures.getFilePath().equals(message.getFilePath())) {
                exists = true;
                break;
            }
            i = i + 1;
        }
        if (exists) {
            selectedMessages.remove(i);
        } else {
            selectedMessages.add(message);
        }
    }


}
