package com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Model;

import android.net.Uri;
import android.util.Log;

import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Interface.IVideo;

import java.util.ArrayList;

/**
 * Created by Carlos Anguiano on 07/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class VideoModel implements IVideo.Model {

    private static final String TAG = VideoModel.class.getSimpleName();
    private IVideo.Presenter presenter;

    public VideoModel(IVideo.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void checkIntentM(ArrayList<String> path) {
        if (path != null) {
            presenter.validPath(path);
        } else {
            presenter.invalidPathError("Error de Path");
        }
    }

    @Override
    public void checkIntentUri(Uri mImagePathVideo) {
        //Todo Checar uri que llegue  a VideoPlayerActivy
        ArrayList<String> arrayList = new ArrayList<>();
        if (mImagePathVideo != null) {
            Uri path = Uri.parse(mImagePathVideo.toString());
            arrayList.add(path.getPath());
            presenter.validPath(arrayList);
        } else {
            presenter.invalidPathError("Error de Path");
        }
    }

    @Override
    public void sendNameOfToolbar(String string) {
        //Enviar el dato a mostrar al toolbar
        presenter.sendDataToolbar(string);
    }

    @Override
    public void showErrorPath() {
        presenter.showErrorAlert();
    }

    @Override
    public void convertArrayList(ArrayList<String> mImagePath) {
        Log.i(TAG, "convertArrayList: ");
        //Convierte un array en string
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : mImagePath) {
            stringBuilder.append(s);
            break;
        }
        String videoPathString = stringBuilder.toString();
        presenter.sendValueOfString(videoPathString);
    }

}
