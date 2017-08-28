package com.carpediemsolution.fitdiary.charts.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.charts.views.CaloriesView;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Weight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Юлия on 28.08.2017.
 */
@InjectViewState
public class CaloriesPresenter extends MvpPresenter<CaloriesView> {

    private List<Weight> weightsForGraph;

    public void init(){
        FitLab fitLab = App.getFitLab();

        if (!DbSchema.CalculatorTable.NAME.isEmpty() && fitLab.getWeights().size() > 0) {
            List<Weight> weights = fitLab.getWeights();
            weightsForGraph = getWeightsForCaloriesGraph(weights);
            getViewState().showSuccess(weightsForGraph);

        } else {
            getViewState().showError();
        }

    }


    private List getWeightsForCaloriesGraph(List <Weight> weights) {
        weightsForGraph = new ArrayList<>();
        for (int i = 0; i < weights.size(); i++) {
            if (weights.get(i).getCalories() != null) {
                weightsForGraph.add(weights.get(i));
            }
        }

        Collections.sort(weightsForGraph, (Weight o1, Weight o2)-> {
                return o1.getDate().compareTo(o2.getDate());
            });
        return weightsForGraph;
    }
}
