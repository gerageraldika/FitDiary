package com.carpediemsolution.fitdiary.utils.asynk_utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.utils.MathUtils;

import java.util.List;

/**
 * Created by Юлия on 06.08.2017.
 */

public class WeightStatisticTask extends AsyncTask<Void, Void, String> {
    private TableLayout tabLayout;
    private List<Weight> weights;
    private Person person;
    private Activity activity;

    public WeightStatisticTask(TableLayout tabLayout, List<Weight> weights, Person person) {
        this.tabLayout = tabLayout;
        this.weights = weights;
        this.person = person;
    }

    public void link(Activity act) {
        activity = act;
    }

    // обнуляем ссылку
    public void unLink() {
        activity = null;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... param) {
        return new MathUtils().getWeightStatistics(weights, person);
    }

    @Override
    protected void onPostExecute(String string) {
        tabLayout.setStretchAllColumns(true);
        tabLayout.bringToFront();

        TableRow tableRowFirst = new TableRow(activity);
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(activity);
        c11.setTextColor(activity.getResources().getColor(R.color.colorWhite));
        c11.setText(activity.getString(R.string.average_weight));
        c11.setPadding(15, 20, 15, 20);
        tableRowFirst.addView(c11);

        TableRow tableRowSecond = new TableRow(activity);
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(activity);
        c22.setTextColor(activity.getResources().getColor(R.color.colorWhite));
        c22.setText(string);
        c22.setPadding(15, 20, 15, 20);
        tableRowSecond.addView(c22);

        tabLayout.addView(tableRowFirst);
        tabLayout.addView(tableRowSecond);
    }
}