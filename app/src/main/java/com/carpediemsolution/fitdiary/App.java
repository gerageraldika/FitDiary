package com.carpediemsolution.fitdiary;

import android.app.Application;
import android.content.Context;

/**
 * Created by Юлия on 02.08.2017.
 */

public class App extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static Context getAppContext() {
        return appContext;
    }
}