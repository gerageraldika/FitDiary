package com.carpediemsolution.fitdiary.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.util.Constants;
import com.carpediemsolution.fitdiary.util.PictureUtils;

import java.io.File;

/**
 * Created by Юлия on 16.03.2017.
 */

public class PhotoViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.open_photo);

        ImageView showPhoto = (ImageView) findViewById(R.id.weight_photo);

        String photoUri = getIntent().getStringExtra(Constants.WEIGHT_URI);

        Uri uri = Uri.parse(photoUri);

        File photoFile = new File(uri.getPath());

        Bitmap bitmap = PictureUtils.getScaledBitmap(
                photoFile.getPath(), this);

        showPhoto.setImageBitmap(bitmap);
        showPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
