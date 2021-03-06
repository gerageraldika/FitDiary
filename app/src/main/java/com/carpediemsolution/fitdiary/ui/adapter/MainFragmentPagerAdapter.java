package com.carpediemsolution.fitdiary.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carpediemsolution.fitdiary.fragment.FitListFragment;
import com.carpediemsolution.fitdiary.fragment.ReminderListFragment;

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
                return new FitListFragment();
            case 1:
                return new ReminderListFragment();
            default:
            return null;
        }
    }
}

