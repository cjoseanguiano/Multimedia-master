package com.bsdenterprise.carlos.anguiano.multimedia.Utils;

import android.app.Application;

/**
 * Created by CarlosAnguiano on 11/09/17.
 */

public class ApplicationSingleton extends Application {

    private static ApplicationSingleton sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = (ApplicationSingleton) getApplicationContext();
    }

    public static ApplicationSingleton getInstance() {
        return sInstance;
    }
}
