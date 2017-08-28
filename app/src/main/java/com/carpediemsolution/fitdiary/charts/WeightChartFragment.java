package com.carpediemsolution.fitdiary.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.charts.presenters.WeightChartPresenter;
import com.carpediemsolution.fitdiary.charts.views.WeightChartView;
import com.carpediemsolution.fitdiary.model.Weight;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Юлия on 15.02.2017.
 */

public class WeightChartFragment extends MvpAppCompatFragment implements WeightChartView {
    //  private static final String GRAPHIC_LOG = "GraphicLog";
    @InjectPresenter
    WeightChartPresenter presenter;
    private LineChart chart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_layout_weight, container, false);
        chart = (LineChart) view.findViewById(R.id.chart);
        presenter.init();
        return view;
    }

    @Override
    public void showSuccess(@NonNull List<Weight> weightList) {
        setData(weightList);
    }

    @Override
    public void showError() {
        Toast toast = Toast.makeText(getActivity(),
                R.string.string_weight_graph, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //success
    private ArrayList<Entry> setYAxisValues(List<Weight> weights) {

        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < weights.size(); i++) {
            float dateForGraph = weights.get(i).getDate().getTime();
            float countForGraph = Float.parseFloat(weights.get(i).getsWeight());
            entries.add(new Entry(dateForGraph, countForGraph));
        }

        Collections.sort(entries, new EntryXComparator());
        return entries;
    }

    private void setData(List<Weight> weights) {

        ArrayList<Entry> yVals = setYAxisValues(weights);
        LineDataSet set1 = new LineDataSet(yVals, " ");
        set1.setFillColor(Color.rgb(97, 97, 97));
        set1.setFillAlpha(140);
        set1.setCircleColor(Color.rgb(105, 158, 58));
        set1.setColor(Color.GRAY);
        set1.setValueTextColor(Color.rgb(105, 158, 58));
        set1.setColors(new int[]{Color.rgb(69, 139, 116), Color.rgb(47, 79, 79)});
        set1.setLineWidth(1f);
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        data.notifyDataChanged();
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
        chart.setScaleMinima(2f, 1f);
        chart.getDescription().setText(" ");

        final ArrayList<Long> xLabel = new ArrayList<>(); //названия по иксу
        for (int i = 0; i < weights.size(); i++) {
            xLabel.add(weights.get(i).getDate().getTime());
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() { //значения по иксу
            private SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM");


            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(mFormat.format(value));
            }
        });
    }
}





















