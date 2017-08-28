package com.carpediemsolution.fitdiary.charts.views;

import com.arellomobile.mvp.MvpView;


/**
 * Created by Юлия on 28.08.2017.
 */

public interface ReminderChartView extends MvpView {

    void showCalories(String s);

    void beforeWeightAverageAsync();

    void showWeightAverage(String s);

    void showWeightStatistic(String s);

    void showWeightResults(String s);

}
