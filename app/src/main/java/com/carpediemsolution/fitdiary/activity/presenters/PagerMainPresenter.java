package com.carpediemsolution.fitdiary.activity.presenters;

import android.widget.ImageView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.activity.views.PagerMainView;
import com.carpediemsolution.fitdiary.util.async.IconPhotoAsync;

/**
 * Created by Юлия on 28.08.2017.
 */
@InjectViewState
public class PagerMainPresenter extends MvpPresenter<PagerMainView>{

    public void init(ImageView imageView){
        IconPhotoAsync iconPhotoTask = new IconPhotoAsync(imageView);
        iconPhotoTask.execute();
    }
}
