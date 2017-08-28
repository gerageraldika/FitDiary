package com.carpediemsolution.fitdiary.presenter;

import android.support.annotation.NonNull;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.view.FitView;

/**
 * Created by Юлия on 27.08.2017.
 */
@InjectViewState
public class FitPresenter extends MvpPresenter<FitView> {

    public void onItemClick(@NonNull Weight weight) {
        getViewState().showDetails(weight);
    }

    public void loadData(){
        //db
        try {
            getViewState().showFitList(App.getFitLab().getWeights());
        }
        catch (Exception e){
            getViewState().showError();
        }
    }

    public void deleteWeight(Weight weight){
        String uuidString = weight.getId().toString();
        App.getFitLab().dataBase.delete(DbSchema.CalculatorTable.NAME,
                DbSchema.CalculatorTable.Cols.UUID + " = '" + uuidString + "'", null);
    }
}
