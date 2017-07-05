package com.carpediemsolution.fitdiary;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.carpediemsolution.fitdiary.adapter.GraphFragmentsPagerAdapter;
import com.carpediemsolution.fitdiary.adapter.LockableViewPager;

/**
 * Created by Юлия on 15.02.2017.
 */

public class GraphicActivity extends AppCompatActivity {

    private TabLayout mTableLayout;
    private LockableViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graph_activity_pager);

        mTableLayout = (TabLayout) findViewById(R.id.tab_graph_layout);
        mViewPager = (LockableViewPager) findViewById(R.id.graph_view_pager);
        mViewPager.setSwipeable(false);

        mViewPager.setAdapter(new GraphFragmentsPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.titles_graph_tab)));

        mTableLayout.setupWithViewPager(mViewPager);

    }
}





