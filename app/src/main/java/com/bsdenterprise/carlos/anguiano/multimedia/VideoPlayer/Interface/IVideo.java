package com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Interface;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Carlos Anguiano on 07/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public interface IVideo {

    interface View {
        void sendValuePath(ArrayList<String> path);

        void sendErrorPath(String error);

        void showDataToolbar(String string);

        void showErrorPathAlert();

        void getValueOfString(String videoPathString);
    }

    interface Model {

        void checkIntentM(ArrayList<String> b);

        void sendNameOfToolbar(String string);

        void showErrorPath();

        void convertArrayList(ArrayList<String> mImagePath);

        void checkIntentUri(Uri mImagePathVideo);
    }

    interface Presenter {

        void checkIntentP(ArrayList<String> b);

        void validPath(ArrayList<String> path);

        void invalidPathError(String error);

        void sendNameToolbar(String string);

        void sendDataToolbar(String string);

        void checkIntentFailed();

        void showErrorAlert();

        void addDataArray(ArrayList<String> mImagePath);

        void sendValueOfString(String videoPathString);

        void checkIntentVideoCapture(Uri mImagePathVideo);

    }
}
