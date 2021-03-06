package com.carpediemsolution.fitdiary.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carpediemsolution.fitdiary.statistic.CaloriesChartFragment;
import com.carpediemsolution.fitdiary.statistic.ReminderChartFragment;
import com.carpediemsolution.fitdiary.statistic.WeightChartFragment;

/**
 * Created by Юлия on 08.03.2017.
 */

public class ChartFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabGraphTitles;

    public ChartFragmentPagerAdapter(FragmentManager fm, String[] mTabTitles){
        super(fm);
        this.mTabGraphTitles = mTabTitles;
    }

    @Override
    public int getCount() {
        return this.mTabGraphTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mTabGraphTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new WeightChartFragment();
            case 1:
                return new CaloriesChartFragment();
            case 2:
                return new ReminderChartFragment();
            default:
                return null;
        }

    }
}

