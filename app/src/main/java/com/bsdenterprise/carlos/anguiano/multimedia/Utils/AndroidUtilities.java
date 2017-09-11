package com.bsdenterprise.carlos.anguiano.multimedia.Utils;

import com.bsdenterprise.carlos.anguiano.multimedia.R;

/**
 * Created by Carlos Anguiano on 11/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class AndroidUtilities {

    public static String cutString(String dataJid, String cutValue) {
        String[] parts = dataJid.split(cutValue);
        String user = parts[0];
        String value = ApplicationSingleton.getInstance().getString(R.string.titleMultimedia);
        String body = String.format(value, user);
        return body;
    }
}
