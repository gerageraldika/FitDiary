package com.carpediemsolution.fitdiary.utils.asynk_utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
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

public class WeightAverageTask extends AsyncTask<Void, Void, String> {
    private TableLayout tabLayout;
    private List<Weight> weights;
    private Activity activity;

    public WeightAverageTask(TableLayout tabLayout, List<Weight> weights) {
        this.tabLayout = tabLayout;
        this.weights = weights;
    }

    public void link(FragmentActivity act) {
        activity = act;
    }

    // обнуляем ссылку
    public void unLink() {
        activity = null;
    }

    @Override
    protected void onPreExecute() {
        tabLayout.setStretchAllColumns(true);
        tabLayout.bringToFront();

        TableRow tableRowFirst = new TableRow(activity);
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(activity);
        c11.setTextColor(activity.getResources().getColor(R.color.colorWhite));
        c11.setText(activity.getString(R.string.changing_weight_by_day));
        c11.setPadding(15, 20, 15, 20);
        tableRowFirst.addView(c11);
        tabLayout.addView(tableRowFirst);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... param) {
        return new MathUtils().getWeightProgress(weights);
    }

    @Override
    protected void onPostExecute(String string) {

        TableRow tableRowSecond = new TableRow(activity);
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(activity);
        c22.setTextColor(activity.getResources().getColor(R.color.colorWhite));
        c22.setText(string);
        c22.setPadding(15, 20, 15, 20);
        tableRowSecond.addView(c22);
        tabLayout.addView(tableRowSecond);
    }
}
