package com.carpediemsolution.fitdiary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carpediemsolution.fitdiary.CalculatorListFragment;
import com.carpediemsolution.fitdiary.ReminderListFragment;

/**
 * Created by Юлия on 04.03.2017.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabTitles;

    public MainFragmentPagerAdapter(FragmentManager fm, String[] mTabTitles){
        super(fm);
        this.mTabTitles = mTabTitles;}

        @Override
        public int getCount() {
            return this.mTabTitles.length;
        }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mTabTitles[position];
    }

    @Override
        public Fragment getItem(int position) {
            switch (position){
            case 0:
                return new CalculatorListFragment();
            case 1:
                return new ReminderListFragment();
            default:
            return null;
        }
    }
}

