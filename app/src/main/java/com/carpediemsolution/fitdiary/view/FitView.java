package com.carpediemsolution.fitdiary.view;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Weight;

import java.util.List;

/**
 * Created by Юлия on 27.08.2017.
 */

public interface FitView extends MvpView, BaseView{

     void showDetails(@NonNull Weight weight);
     void showFitList(List<Weight> weightList);

}
