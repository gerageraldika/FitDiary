package com.carpediemsolution.fitdiary.presenter;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.view.FitDetailsView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Юлия on 27.08.2017.
 */
@InjectViewState
public class FitDetailsPresenter extends MvpPresenter<FitDetailsView> {


    public void getWeight(UUID uuid) {
        getViewState().showFitDetails(FitLab.get().getWeight(uuid));
    }

    public void updateWeight(UUID uuid, String weight, String calories, String notes, Date date) {

        if (TextUtils.isEmpty(weight)) {
            getViewState().showError();
        } else if (Double.parseDouble(weight) <= 300) {
            Weight userWeight = new Weight(uuid);

            userWeight.setWeight(weight);
            if (!TextUtils.isEmpty(calories))
                userWeight.setCalories(calories);
            if (!TextUtils.isEmpty(notes))
                userWeight.setNotes(notes);
            userWeight.setDate(date);

            FitLab.get().updateWeight(userWeight);
        }
        else getViewState().showErrorMessage();
    }
}
