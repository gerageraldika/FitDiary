package com.carpediemsolution.fitdiary.utils.asynk_utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.utils.MathUtils;

import java.util.List;

/**
 * Created by Юлия on 06.08.2017.
 */

public class CaloriesStaticticTask extends AsyncTask<Void, Void, String> {
    private TableLayout tabLayout;
    private List<Weight> weights;
    private Context mContext;

    public CaloriesStaticticTask(TableLayout tabLayout, List<Weight> weights, Context context) {
        this.tabLayout = tabLayout;
        this.weights = weights;
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Void... param) {
        return new MathUtils().getAverageCalories(weights);
    }

    @Override
    protected void onPostExecute(String string) {
        tabLayout.setStretchAllColumns(true);
        tabLayout.bringToFront();

        TableRow tableRowFirst = new TableRow(mContext);
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(mContext);
        c11.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        c11.setText(mContext.getString(R.string.average_calories));
        // c11.setTextSize(15);
        c11.setPadding(15, 30, 15, 30);
        tableRowFirst.addView(c11);

        TableRow tableRowSecond = new TableRow(mContext);
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(mContext);
        c22.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        c22.setText(string);
        c22.setPadding(15, 20, 15, 20);

        tableRowSecond.addView(c22);

        tabLayout.addView(tableRowFirst);
        tabLayout.addView(tableRowSecond);
    }
}

