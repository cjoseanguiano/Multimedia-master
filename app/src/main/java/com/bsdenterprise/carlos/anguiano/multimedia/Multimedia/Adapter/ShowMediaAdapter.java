package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Utils.TouchImageView;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Carlos Anguiano on 06/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class ShowMediaAdapter extends PagerAdapter {
    private ArrayList<String> mImagePath = new ArrayList<>();
    private LayoutInflater inflater;
    private Activity activity;

    @Override
    public int getItemPosition(Object object) {
        if (mImagePath != null) {
            for (int i = 0; i < mImagePath.size(); i++) {
                if (mImagePath.get(i).equals(object))
                    return i;
            }
        }
        return 0;
    }

    public ShowMediaAdapter(Activity act, ArrayList<String> imagePaths) {
        this.activity = act;
        this.mImagePath = imagePaths;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.mImagePath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position + " of " + getCount();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final TouchImageView imgDisplay;

        View viewLayout = inflater.inflate(R.layout.item_fullscreen_multimedia, container, false);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);

        String localPath = mImagePath.get(position);
        File file = new File(localPath);
        if (file.exists()) {
            Glide.with(activity)
                    .load(new File(localPath))
                    .into(imgDisplay);
            container.addView(viewLayout);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("Error de Imagen");
            alertDialog.setMessage("Verifica que la imagen exista dentro de la tarjeta SD");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    activity.finish();
                }
            });
            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog.show();
        }


        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);

    }

    @Nullable
    public String getCurrentItem(int position) {
        if (mImagePath.size() > 0) {
            return mImagePath.get(position);
        } else
            return null;
    }

    public void refreshData(ArrayList<String> path) {
        mImagePath = new ArrayList<>(path);
        notifyDataSetChanged();
    }
}
