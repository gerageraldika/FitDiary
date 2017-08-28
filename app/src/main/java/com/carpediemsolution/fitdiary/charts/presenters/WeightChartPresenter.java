package com.carpediemsolution.fitdiary.charts.presenters;

import android.view.Gravity;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.charts.views.ReminderChartView;
import com.carpediemsolution.fitdiary.charts.views.WeightChartView;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Weight;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Юлия on 28.08.2017.
 */
@InjectViewState
public class WeightChartPresenter extends MvpPresenter<WeightChartView> {

    public void init(){
        FitLab fitLab = App.getFitLab();

        if (!DbSchema.CalculatorTable.NAME.isEmpty() && fitLab.getWeights().size() > 0) {

           List<Weight> weights = fitLab.getWeights();

            getViewState().showSuccess(weights);

        } else {
            getViewState().showError();

        }
    }
}
