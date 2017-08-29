package com.carpediemsolution.fitdiary.statistic.views;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.RemindsCounter;

import java.util.List;


/**
 * Created by Юлия on 28.08.2017.
 */

public interface ReminderChartView extends MvpView {

    void showCalories(String s);

    void beforeWeightAverageAsync();

    void showWeightAverage(String s);

    void showWeightStatistic(String s);

    void showWeightResults(String s);

    void showReminderStatistic(List<RemindsCounter> reminderCounters);

    void showError();

}
