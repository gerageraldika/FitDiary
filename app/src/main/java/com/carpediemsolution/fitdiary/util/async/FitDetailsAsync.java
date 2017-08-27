package com.carpediemsolution.fitdiary.util.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Weight;

import java.io.File;
import java.io.IOException;

/**
 * Created by Юлия on 27.08.2017.
 */


public class FitDetailsAsync extends AsyncTask<Void, Void, Bitmap> {
    private ImageView photoView;
    private Weight weight;
    private boolean isEnabledPhoto;

    public FitDetailsAsync(ImageView mPhotoView, Weight mWeight, boolean isEnabledPhoto) {
        this.photoView = mPhotoView;
        this.weight = mWeight;
        this.isEnabledPhoto = isEnabledPhoto;
    }

    @Override
    protected Bitmap doInBackground(Void... param) {
        Bitmap bitmap = null;
        try {
            bitmap = getPhotoFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap == null) {
            photoView.setImageResource(R.drawable.ic_perm_identity_white_24dp);
        } else {
            photoView.setImageBitmap(bitmap);
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            isEnabledPhoto = false;
        }
    }

    private Bitmap getPhotoFile() throws NullPointerException, IOException {
        Uri imageUri = Uri.parse(weight.getPhotoUri());
        File photoFile = new File(imageUri.getPath());

        Bitmap bitmap;
        if (photoFile.exists()) {
            bitmap = BitmapFactory.decodeFile(photoFile.getPath());

        } else bitmap = null;
        return bitmap;
    }
}

