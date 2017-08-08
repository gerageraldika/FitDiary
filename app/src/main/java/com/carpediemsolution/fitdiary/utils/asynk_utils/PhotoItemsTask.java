package com.carpediemsolution.fitdiary.utils.asynk_utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.utils.PictureUtils;

import java.io.File;

/**
 * Created by Юлия on 08.08.2017.
 */

public class PhotoItemsTask extends AsyncTask<Void, Void, Bitmap> {
    private ImageView mPhotoView;
    private Weight mWeight;
    private FragmentActivity activity;

    public PhotoItemsTask(ImageView mPhotoView, Weight mWeight, FragmentActivity activity) {
        this.mPhotoView = mPhotoView;
        this.mWeight = mWeight;
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(Void... param) {
        Bitmap bitmap;
        try {
            Uri imageUri = Uri.parse(mWeight.getPhotoUri());
            File photoFile = new File(imageUri.getPath());

            bitmap = PictureUtils.getScaledBitmap(
                    photoFile.getPath(), activity);

        } catch (NullPointerException e) {
            bitmap = null;}
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap = PictureUtils.cropToSquare(bitmap);
            RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.
                    create(activity.getResources(), bitmap);
            roundDrawable.setCircular(true);

            mPhotoView.setImageDrawable(roundDrawable);
            mPhotoView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mPhotoView.setImageResource(R.drawable.ic_perm_identity_white_24dp);
        }
    }
}
