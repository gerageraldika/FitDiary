package com.carpediemsolution.fitdiary.charts;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.activity.PagerMainActivity;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.charts.presenters.ReminderChartPresenter;
import com.carpediemsolution.fitdiary.charts.views.ReminderChartView;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.RemindsCounter;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.util.OnBackListener;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Юлия on 08.03.2017.
 */

public class ReminderChartFragment extends MvpAppCompatFragment implements OnBackListener,
        ReminderChartView {
    // private static final String GRAPHIC_LOG = "ReminderChartFragment";
    @InjectPresenter
    ReminderChartPresenter presenter;
    @BindView(R.id.tab_calories_layout)
    TableLayout caloriesTab;
    @BindView(R.id.tab_weight_layout_1)
    TableLayout weightAverageTab;
    @BindView(R.id.tab_weight_layout_2)
    TableLayout weightStatisticsTab;
    @BindView(R.id.tab_weight_layout_3)
    TableLayout weightResultsTab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setRetainInstance(true); ??
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_layout_reminds, container, false);
        ButterKnife.bind(this, view);
        TableLayout tableLayoutForReminds = (TableLayout) view.findViewById(R.id.tab_remind_layout);

        FitLab sCalcLab = FitLab.get();
        if (!DbSchema.CalculatorTable.NAME_COUNTER.isEmpty() &&
                sCalcLab.getRemindCounts().size() > 0) {
            List<RemindsCounter> reminderCounters = sCalcLab.getRemindCounts();
            getRemindsStatistic(reminderCounters, tableLayoutForReminds);
        }

        if (!DbSchema.CalculatorTable.NAME.isEmpty() && sCalcLab.getWeights().size() > 0) {
            List<Weight> weights = sCalcLab.getWeights();
            Person person = sCalcLab.getPerson();
            presenter.init(weights, person);
        } else {
            Toast toast = Toast.makeText(getActivity(),
                    R.string.string_weight_graph, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return view;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getActivity(), PagerMainActivity.class);
        startActivity(i);
    }

    @Override
    public void showCalories(String s) {
        caloriesTab.setStretchAllColumns(true);
        caloriesTab.bringToFront();

        TableRow tableRowFirst = new TableRow(getActivity());
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(getActivity());
        c11.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c11.setText(getActivity().getString(R.string.average_calories));
        c11.setPadding(15, 30, 15, 30);
        tableRowFirst.addView(c11);

        TableRow tableRowSecond = new TableRow(getActivity());
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(getActivity());
        c22.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c22.setText(s);
        c22.setPadding(15, 20, 15, 20);

        tableRowSecond.addView(c22);

        caloriesTab.addView(tableRowFirst);
        caloriesTab.addView(tableRowSecond);
    }

    @Override
    public void beforeWeightAverageAsync() {
        weightAverageTab.setStretchAllColumns(true);
        weightAverageTab.bringToFront();

        TableRow tableRowFirst = new TableRow(getActivity());
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(getActivity());
        c11.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c11.setText(getActivity().getString(R.string.changing_weight_by_day));
        c11.setPadding(15, 20, 15, 20);
        tableRowFirst.addView(c11);
    }

    @Override
    public void showWeightAverage(String s) {
        TableRow tableRowSecond = new TableRow(getActivity());
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(getActivity());
        c22.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c22.setText(s);
        c22.setPadding(15, 20, 15, 20);
        tableRowSecond.addView(c22);
        weightAverageTab.addView(tableRowSecond);
    }

    @Override
    public void showWeightStatistic(String s) {
        weightStatisticsTab.setStretchAllColumns(true);
        weightStatisticsTab.bringToFront();

        TableRow tableRowFirst = new TableRow(getActivity());
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(getActivity());
        c11.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c11.setText(getActivity().getString(R.string.average_weight));
        c11.setPadding(15, 20, 15, 20);
        tableRowFirst.addView(c11);

        TableRow tableRowSecond = new TableRow(getActivity());
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(getActivity());
        c22.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c22.setText(s);
        c22.setPadding(15, 20, 15, 20);
        tableRowSecond.addView(c22);

        weightStatisticsTab.addView(tableRowFirst);
        weightStatisticsTab.addView(tableRowSecond);
    }

    @Override
    public void showWeightResults(String s) {
        weightResultsTab.setStretchAllColumns(true);
        weightResultsTab.bringToFront();

        TableRow tableRowFirst = new TableRow(getActivity());
        tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
        TextView c11 = new TextView(getActivity());
        c11.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c11.setText(getActivity().getString(R.string.from_start_changing_weight));
        c11.setPadding(15, 20, 15, 20);
        tableRowFirst.addView(c11);

        TableRow tableRowSecond = new TableRow(getActivity());
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(getActivity());
        c22.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        c22.setText(s);
        c22.setPadding(15, 20, 15, 20);
        tableRowSecond.addView(c22);

        weightResultsTab.addView(tableRowFirst);
        weightResultsTab.addView(tableRowSecond);
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
            c2.setTextColor(App.getAppContext().getResources().getColor(R.color.colorWhite));
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
}

