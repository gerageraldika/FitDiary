package com.carpediemsolution.fitdiary.util.async;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.util.PictureUtils;

import java.io.File;

/**
 * Created by Юлия on 08.08.2017.
 */

public class PhotoItemTask extends AsyncTask<Void, Void, Bitmap> {
    private ImageView photoView;
    private Weight weight;

    public PhotoItemTask(ImageView mPhotoView, Weight mWeight) {
        this.photoView = mPhotoView;
        this.weight = mWeight;
    }

    @Override
    protected Bitmap doInBackground(Void... param) {
        Bitmap bitmap;
        try {
            Uri imageUri = Uri.parse(weight.getPhotoUri());
            File photoFile = new File(imageUri.getPath());

            bitmap = BitmapFactory.decodeFile(photoFile.getPath());

        } catch (NullPointerException e) {
            bitmap = null;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap = PictureUtils.cropToSquare(bitmap);
            RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.
                    create(App.getAppContext().getResources(), bitmap);
            roundDrawable.setCircular(true);

            photoView.setImageDrawable(roundDrawable);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            photoView.setImageResource(R.drawable.ic_perm_identity_white_24dp);
        }
    }
}
