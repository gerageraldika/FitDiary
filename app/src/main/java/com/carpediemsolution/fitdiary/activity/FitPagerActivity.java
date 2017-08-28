package com.carpediemsolution.fitdiary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.fragment.FitDetailsFragment;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.util.OnBackListener;

import java.util.List;
import java.util.UUID;

/**
 * Created by Юлия on 11.02.2017.
 */

public class FitPagerActivity extends AppCompatActivity implements OnBackListener {

    private List<Weight> weightList;
    private static final String EXTRA_WEIGHT_ID = "com.carpediemsolution.fitdiary.weight_id";

    public static Intent newIntent(Context packageContext, UUID weightId) {
        Intent intent = new Intent(packageContext, FitPagerActivity.class);
        intent.putExtra(EXTRA_WEIGHT_ID, weightId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_pager);

        UUID weightId = (UUID) getIntent().getSerializableExtra(EXTRA_WEIGHT_ID);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.activity_calc_pager_view_pager);
        weightList = App.getFitLab().getWeights();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Weight mWeight = weightList.get(position);
                return FitDetailsFragment.newInstance(mWeight.getId());
            }

            @Override
            public int getCount() {
                return weightList.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Weight weight = weightList.get(position);
                if (weight.getsWeight() != null) {
                    setTitle(weight.getsWeight());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        for (int i = 0; i < weightList.size(); i++) {
            if (weightList.get(i).getId().equals(weightId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
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
}







