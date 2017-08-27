package com.carpediemsolution.fitdiary.charts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.carpediemsolution.fitdiary.activity.PagerMainActivity;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.RemindsCounter;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.util.OnBackListener;
import com.carpediemsolution.fitdiary.util.async.CaloriesAsync;
import com.carpediemsolution.fitdiary.util.async.WeightAverageAsync;
import com.carpediemsolution.fitdiary.util.async.WeightResultAsync;
import com.carpediemsolution.fitdiary.util.async.WeightStatisticAsync;

import java.util.List;

/**
 * Created by Юлия on 08.03.2017.
 */

public class RemindsChartFragment extends Fragment implements OnBackListener {

   // private static final String GRAPHIC_LOG = "RemindsChartFragment";
    private CaloriesAsync caloriesTask;
    private WeightAverageAsync weightAverageTask;
    private WeightStatisticAsync weightStatisticTask;
    private WeightResultAsync weightResultTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_layout_reminds, container, false);

        TableLayout tableLayoutForReminds = (TableLayout) view.findViewById(R.id.tab_remind_layout);
        TableLayout tabLayoutForWeights = (TableLayout) view.findViewById(R.id.tab_weight_layout_1);
        TableLayout tableLayoutForWeightStatistics = (TableLayout) view.findViewById(R.id.tab_weight_layout_2);
        TableLayout tableLayoutForWeightResults = (TableLayout) view.findViewById(R.id.tab_weight_layout_3);
        TableLayout tableLayoutAverageCalories = (TableLayout) view.findViewById(R.id.tab_calories_layout);

        FitLab sCalcLab = FitLab.get();
        if (!DbSchema.CalculatorTable.NAME_COUNTER.isEmpty() &&
                sCalcLab.getRemindCounts().size() > 0) {
            List<RemindsCounter> reminderCounters = sCalcLab.getRemindCounts();
            getRemindsStatistic(reminderCounters, tableLayoutForReminds);
        }

        if (!DbSchema.CalculatorTable.NAME.isEmpty() && sCalcLab.getWeights().size() > 0) {
            List<Weight> weights = sCalcLab.getWeights();
            Person person = sCalcLab.getPerson();

            weightAverageTask = new WeightAverageAsync(tabLayoutForWeights, weights);
            weightAverageTask.link(getActivity());
            weightAverageTask.execute();
            // передаем в Task ссылку на текущее Activity

            weightStatisticTask = new WeightStatisticAsync(tableLayoutForWeightStatistics, weights, person);
            weightStatisticTask.link(getActivity());
            weightStatisticTask.execute();

            weightResultTask = new WeightResultAsync(tableLayoutForWeightResults,weights,person);
            weightResultTask.link(getActivity());
            weightResultTask.execute();

            caloriesTask = new CaloriesAsync(tableLayoutAverageCalories, weights);
            caloriesTask.link(getActivity());
            caloriesTask.execute();
        } else {
            Toast toast = Toast.makeText(getActivity(),
                    R.string.string_weight_graph, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return view;
    }

    public void getRemindsStatistic(List<RemindsCounter> reminderCounters, TableLayout tabLayout) {

        tabLayout.setStretchAllColumns(true);
        tabLayout.bringToFront();

        TableRow tableRowFirst = new TableRow(getActivity());
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(getActivity());
        c11.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c11.setText(getActivity().getString(R.string.from_start_remind_graph_date));
        // c11.setTextSize(15);
        c11.setPadding(15, 20, 15, 20);
        c11.setBackgroundResource(R.color.colorDeepGreen);
        c11.setGravity(Gravity.CENTER);
        tableRowFirst.addView(c11);
        tabLayout.addView(tableRowFirst);


        for (int i = 0; i < reminderCounters.size(); i++) {
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tableRow.setBackgroundResource(R.drawable.rectangle_graph_title);
            TextView c1 = new TextView(getActivity());
            c1.setWidth(50);
            c1.setTextColor(getActivity().getResources().getColor(R.color.colorDeepOrange));
            c1.setGravity(Gravity.CENTER);
            c1.setBackgroundResource(R.drawable.rectangle_graph_grey);
            c1.setPadding(0, 20, 0, 20);
            c1.setText(DateFormat.format("dd MM yyyy", reminderCounters.get(i).getDate()));


            TextView c2 = new TextView(getActivity());
            c2.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            c2.setGravity(Gravity.CENTER);
            c2.setPadding(0, 20, 0, 20);
            c2.setBackgroundResource(R.drawable.rectangle_graph_grey);
            c2.setText(String.valueOf(reminderCounters.get(i).getCounterFlag()));
            tableRow.addView(c1, new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
            tableRow.addView(c2, new TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            tabLayout.addView(tableRow);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(caloriesTask!=null)
            caloriesTask.unLink();
        if(weightAverageTask!=null)
            weightAverageTask.unLink();
        if(weightStatisticTask!=null)
            weightStatisticTask.unLink();
        if(weightResultTask!=null)
            weightResultTask.unLink();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getActivity(), PagerMainActivity.class);
        startActivity(i);
    }
}

