package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Utils;

import android.content.res.Resources;

import com.bsdenterprise.carlos.anguiano.multimedia.R;

/**
 * Created by CarlosAnguiano on 11/09/17.
 */

public class Components {
    public static String getString(String data, String dataJid) {

        Resources resources = Resources.getSystem();
        String value = String.format(resources.getString(R.string.titleMultimedia, data, dataJid));

        return value;
    }
}
