package com.carpediemsolution.fitdiary.utils.asynk_utils;

import android.content.Context;
import android.os.AsyncTask;
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

public class WeightResultTask extends AsyncTask<Void, Void, String> {
    private TableLayout tabLayout;
    private List<Weight> weights;
    private Person person;
    private Context context;

    public WeightResultTask(TableLayout tabLayout, List<Weight> weights, Person person, Context context) {
        this.tabLayout = tabLayout;
        this.weights = weights;
        this.person = person;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... param) {
        return new MathUtils().getWeightResults(weights, person);
    }

    @Override
    protected void onPostExecute(String string) {
        tabLayout.setStretchAllColumns(true);
        tabLayout.bringToFront();

        TableRow tableRowFirst = new TableRow(context);
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(context);
        c11.setTextColor(context.getResources().getColor(R.color.colorWhite));
        c11.setText(context.getString(R.string.from_start_changing_weight));
        c11.setPadding(15, 20, 15, 20);
        tableRowFirst.addView(c11);

        TableRow tableRowSecond = new TableRow(context);
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(context);
        c22.setTextColor(context.getResources().getColor(R.color.colorWhite));
        c22.setText(string);
        c22.setPadding(15, 20, 15, 20);
        tableRowSecond.addView(c22);

        tabLayout.addView(tableRowFirst);
        tabLayout.addView(tableRowSecond);
    }
}