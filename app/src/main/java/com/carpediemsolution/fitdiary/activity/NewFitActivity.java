package com.carpediemsolution.fitdiary.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.carpediemsolution.fitdiary.fragment.NewFitFragment;
import com.carpediemsolution.fitdiary.util.OnBackListener;
import com.carpediemsolution.fitdiary.ui.adapter.SingleFragmentActivity;

/**
 * Created by Юлия on 18.02.2017.
 */

public class NewFitActivity extends SingleFragmentActivity implements OnBackListener {

    // private static final String LOG_LF = "LifeCycle NewActivity";

    @Override
    protected Fragment createFragment() {
        return new NewFitFragment();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackListener) {
                backPressedListener = (OnBackListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    //just for studying
    @Override
    protected void onStop() {
        super.onStop();
    }
}









