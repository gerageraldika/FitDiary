package com.carpediemsolution.fitdiary.view;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Юлия on 27.08.2017.
 */

public interface NewFitView extends MvpView, BaseView {

    void showInfo(@NonNull String weight);

    void showErrorMessage();
}
