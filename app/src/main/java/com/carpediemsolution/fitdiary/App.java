package com.carpediemsolution.fitdiary;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DBBaseHelper;

/**
 * Created by Юлия on 02.08.2017.
 */

public class App extends Application {

    private static Context appContext;
    private static FitLab fitLab;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        fitLab = get();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static FitLab getFitLab(){
        return fitLab;
    }

    private static FitLab get() {
        if (fitLab == null) {
            SQLiteDatabase database = new DBBaseHelper(App.getAppContext())
                    .getWritableDatabase();
            fitLab = new FitLab(database);
        }
        return fitLab;
    }
}