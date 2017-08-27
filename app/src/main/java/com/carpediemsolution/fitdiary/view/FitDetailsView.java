package com.carpediemsolution.fitdiary.view;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Weight;

/**
 * Created by Юлия on 27.08.2017.
 */

public interface FitDetailsView extends MvpView, BaseView {

    void showFitDetails(Weight weight);

    void showErrorMessage();
}
