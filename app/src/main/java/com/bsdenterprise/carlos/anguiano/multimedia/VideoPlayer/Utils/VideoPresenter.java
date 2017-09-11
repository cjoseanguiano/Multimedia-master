package com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Utils;

import android.util.Log;

import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Interface.IVideo;
import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Model.VideoModel;

import java.util.ArrayList;

/**
 * Created by Carlos Anguiano on 07/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class VideoPresenter implements IVideo.Presenter {

    private static final String TAG = VideoPresenter.class.getSimpleName();
    private IVideo.View view;
    private IVideo.Model model;

    public VideoPresenter(IVideo.View view) {
        this.view = view;
        model = new VideoModel(this);
    }

    @Override
    public void checkIntentP(ArrayList<String> path) {
        Log.i(TAG, "checkIntentP: ");
        if (view != null) {
            model.checkIntentM(path);
        }
    }

    @Override
    public void validPath(ArrayList<String> path) {
        Log.i(TAG, "validPath: ");
        if (view != null) {
            //Toma el valor del path si el valor es valido
            view.sendValuePath(path);
        }

    }

    @Override
    public void invalidPathError(String error) {
        Log.i(TAG, "invalidPathError: ");
        if (view != null) {
            //Se envia un error a la vista por que el path es invalido
            view.sendErrorPath(error);
        }
    }

    @Override
    public void sendNameToolbar(String string) {
        Log.i(TAG, "sendNameToolbar: ");
        if (view != null) {
            model.sendNameOfToolbar(string);
        }
    }

    @Override
    public void sendDataToolbar(String string) {
        Log.i(TAG, "sendDataToolbar: ");
        if (view != null) {
            view.showDataToolbar(string);
        }
    }

    @Override
    public void checkIntentFailed() {
        Log.i(TAG, "checkIntentFailed: ");
        if (view != null) {
            //Si el intent no contiene nada mostrar error (AlertDialog)
            model.showErrorPath();
        }
    }

    @Override
    public void showErrorAlert() {
        view.showErrorPathAlert();
    }

    @Override
    public void addDataArray(ArrayList<String> mImagePath) {
        Log.i(TAG, "addDataArray: ");
        if (view != null) {
            //Convierte un array en string
            model.convertArrayList(mImagePath);
        }
    }

    @Override
    public void sendValueOfString(String videoPathString) {
        // Envia el valor convertido del Array en string
        view.getValueOfString(videoPathString);
    }
}

