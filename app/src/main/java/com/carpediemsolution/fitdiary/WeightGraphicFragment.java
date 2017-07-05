package com.carpediemsolution.fitdiary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.carpediemsolution.fitdiary.database.CalculatorDbSchema;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.utils.CalculatorLab;
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

public class WeightGraphicFragment extends Fragment {

    private String GRAPHIC_LOG = "GraphicLog";
    LineChart chart;
    List<Weight> weights = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ArrayList<Entry> setYAxisValues() {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < weights.size(); i++) {
            float dateForGraph = weights.get(i).getDate().getTime();
            float countForGraph = Float.parseFloat(weights.get(i).getsWeight());
            entries.add(new Entry(dateForGraph, countForGraph));
        }

        Collections.sort(entries, new EntryXComparator());
        return entries;
    }

    private void setData() {

        ArrayList<Entry> yVals = setYAxisValues();
        LineDataSet set1 = new LineDataSet(yVals, " ");
        set1.setFillColor(Color.rgb(97,97,97));
        set1.setFillAlpha(140);
        set1.setCircleColor(Color.rgb(105,158,58));
        set1.setColor(Color.GRAY);
        set1.setValueTextColor(Color.rgb(105,158,58));
        set1.setColors(new int[]{Color.rgb(69,139,116), Color.rgb(47,79,79)});
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_layout_weight, container, false);
        Log.d(GRAPHIC_LOG, "----Start GraphView---");

        CalculatorLab calculatorLab = CalculatorLab.get(getActivity());

          if (!CalculatorDbSchema.CalculatorTable.NAME.isEmpty()&& calculatorLab.getWeights().size() > 0 ) {

            weights = calculatorLab.getWeights();

            chart = (LineChart) view.findViewById(R.id.chart);

            setData();

        } else {
            Toast toast = Toast.makeText(getActivity(),
                    R.string.string_weight_graph, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        return view;

    }
}






















        /*List<Weight> weights = calculatorLabLab.getWeights();

        Date dateForGraph;
        long weightForGraph;

        graphView = (GraphView) view.findViewById(R.id.graphic_layout);
        graphView.setTitleTextSize(5);
        graphView.getGraphContentHeight();
        graphView.getGraphContentWidth();
        graphView.getSecondScale();


        barseries = new BarGraphSeries<DataPoint>();
        for (int i = 0; i < weights.size(); i++) {

            dateForGraph = weights.get(i).getDate();
            weightForGraph = Long.parseLong(weights.get(i).getsWeight().toString());

            barseries.appendData(new DataPoint(dateForGraph, weightForGraph),false,weights.size());
        }

        graphView.addSeries(barseries);

       /* series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < weights.size(); i++) {

            dateForGraph = weights.get(i).getDate();
            weightForGraph = Long.parseLong(weights.get(i).getsWeight().toString());

            series.appendData(new DataPoint(dateForGraph, weightForGraph), true, 2000);
        }
        graphView.addSeries(series);


        // // only 4 because of the space

*/      /*graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);

        barseries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 20);
            }
        });
*/





