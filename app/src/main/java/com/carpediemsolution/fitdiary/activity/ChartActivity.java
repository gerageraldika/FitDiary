package com.carpediemsolution.fitdiary.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.ui.adapter.ChartFragmentPagerAdapter;
import com.carpediemsolution.fitdiary.ui.adapter.LockableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Юлия on 15.02.2017.
 */

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.tab_graph_layout)
    TabLayout tableLayout;
    @BindView(R.id.graph_view_pager)
    LockableViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity_pager);
        ButterKnife.bind(this);

        viewPager.setSwipeable(false);

        viewPager.setAdapter(new ChartFragmentPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.titles_graph_tab)));

        tableLayout.setupWithViewPager(viewPager);
    }
}





