package com.carpediemsolution.fitdiary.view;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Weight;

/**
 * Created by Юлия on 27.08.2017.
 */

public interface FitDetailsView extends MvpView, BaseView {

    void showFitDetails(@NonNull Weight weight);

    void showErrorMessage();
}
