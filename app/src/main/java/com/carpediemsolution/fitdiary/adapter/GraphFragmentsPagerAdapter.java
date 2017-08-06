package com.carpediemsolution.fitdiary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carpediemsolution.fitdiary.charts.CaloriesChartFragment;
import com.carpediemsolution.fitdiary.charts.RemindsChartFragment;
import com.carpediemsolution.fitdiary.charts.WeightChartFragment;

/**
 * Created by Юлия on 08.03.2017.
 */

public class GraphFragmentsPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabGraphTitles;

    public GraphFragmentsPagerAdapter(FragmentManager fm, String[] mTabTitles){
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
                return new RemindsChartFragment();
            default:
                return null;
        }

    }
}

