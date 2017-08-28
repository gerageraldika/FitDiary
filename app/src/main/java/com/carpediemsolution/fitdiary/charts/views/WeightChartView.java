package com.carpediemsolution.fitdiary.charts.views;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Weight;

import java.util.List;

/**
 * Created by Юлия on 28.08.2017.
 */

public interface WeightChartView extends MvpView{

    void showSuccess(@NonNull List<Weight> weightList);

    void showError();
}
