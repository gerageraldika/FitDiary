package com.carpediemsolution.fitdiary.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.ui.adapter.ChartFragmentPagerAdapter;
import com.carpediemsolution.fitdiary.ui.adapter.LockableViewPager;

/**
 * Created by Юлия on 15.02.2017.
 */

public class ChartActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chart_activity_pager);

        TabLayout tableLayout = (TabLayout) findViewById(R.id.tab_graph_layout);
        LockableViewPager mViewPager = (LockableViewPager) findViewById(R.id.graph_view_pager);
        mViewPager.setSwipeable(false);

        mViewPager.setAdapter(new ChartFragmentPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.titles_graph_tab)));

        tableLayout.setupWithViewPager(mViewPager);
    }
}





