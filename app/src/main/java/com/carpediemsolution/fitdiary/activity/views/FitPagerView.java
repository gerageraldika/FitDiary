package com.carpediemsolution.fitdiary.activity.views;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Weight;

import java.util.List;

/**
 * Created by Юлия on 28.08.2017.
 */

public interface FitPagerView extends MvpView {

    void showSuccess(@NonNull List<Weight> weightList);
}
