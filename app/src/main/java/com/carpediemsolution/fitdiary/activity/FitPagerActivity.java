package com.carpediemsolution.fitdiary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.activity.presenters.FitPagerPresenter;
import com.carpediemsolution.fitdiary.activity.views.FitPagerView;
import com.carpediemsolution.fitdiary.fragment.FitDetailsFragment;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.util.OnBackListener;
import java.util.List;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Юлия on 11.02.2017.
 */

public class FitPagerActivity extends MvpAppCompatActivity implements OnBackListener, FitPagerView {

    @InjectPresenter
    FitPagerPresenter presenter;
    @BindView(R.id.activity_calc_pager_view_pager)
    ViewPager viewPager;
    private UUID weightId;
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
        ButterKnife.bind(this);
        weightId = (UUID) getIntent().getSerializableExtra(EXTRA_WEIGHT_ID);
        presenter.init();
    }

    @Override
    public void showSuccess(@NonNull List<Weight> weightList) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
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

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                viewPager.setCurrentItem(i);
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







