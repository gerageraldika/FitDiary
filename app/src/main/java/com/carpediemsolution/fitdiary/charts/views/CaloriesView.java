package com.carpediemsolution.fitdiary.charts.views;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Weight;
import java.util.List;

/**
 * Created by Юлия on 28.08.2017.
 */

public interface CaloriesView extends MvpView{

    void showSuccess(List<Weight> weights);

    void showError();

}
