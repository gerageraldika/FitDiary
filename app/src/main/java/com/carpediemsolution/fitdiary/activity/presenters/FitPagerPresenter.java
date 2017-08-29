package com.carpediemsolution.fitdiary.activity.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.activity.views.FitPagerView;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.model.Weight;

import java.util.List;

/**
 * Created by Юлия on 28.08.2017.
 */
@InjectViewState
public class FitPagerPresenter extends MvpPresenter<FitPagerView> {

    public void init(){
        FitLab fitLab = App.getFitLab();
        List<Weight> weightList = fitLab.getWeights();
        getViewState().showSuccess(weightList);
    }
}
