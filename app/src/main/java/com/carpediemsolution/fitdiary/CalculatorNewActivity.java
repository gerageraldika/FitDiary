package com.carpediemsolution.fitdiary;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.carpediemsolution.fitdiary.utils.OnBackListener;
import com.carpediemsolution.fitdiary.adapter.SingleFragmentActivity;

/**
 * Created by Юлия on 18.02.2017.
 */

public class CalculatorNewActivity extends SingleFragmentActivity implements OnBackListener {

    @Override
    protected Fragment createFragment() {
        return new CalculatorNewFragment();}

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackListener) {
                backPressedListener = (OnBackListener) fragment;
                break;}
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();}
    }
}









