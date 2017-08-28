package com.carpediemsolution.fitdiary.util.async;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.carpediemsolution.fitdiary.App;


/**
 * Created by Юлия on 27.08.2017.
 */


public class IconPhotoAsync extends AsyncTask<Void, Void, Bitmap> {
    private ImageView photoView;

    public IconPhotoAsync(ImageView mPhotoView) {
        this.photoView = mPhotoView;
    }


    @Override
    protected Bitmap doInBackground(Void... param) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences
                (App.getAppContext());
        String imagePath = prefs.getString("Image", "");
        Bitmap bitmap = null;
        if (!imagePath.equals("")) {
            bitmap = BitmapFactory.decodeFile(imagePath);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            photoView.setImageBitmap(bitmap);
        }
    }
}

