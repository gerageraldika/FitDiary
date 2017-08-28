package com.carpediemsolution.fitdiary.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.charts.presenters.CaloriesPresenter;
import com.carpediemsolution.fitdiary.charts.views.CaloriesView;

import com.carpediemsolution.fitdiary.model.Weight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Юлия on 08.03.2017.
 */

public class CaloriesChartFragment extends MvpAppCompatFragment implements CaloriesView {

    @InjectPresenter
    CaloriesPresenter presenter;
    private ColumnChartView chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chart_layout_calories, container, false);
        chart = (ColumnChartView) view.findViewById(R.id.chart);
        presenter.init();

        return view;
    }

    @Override
    public void showSuccess(List<Weight> weights) {
        chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setOnValueTouchListener(new CaloriesChartFragment.ValueTouchListener(weights));
        generateDefaultData(weights);
    }

    @Override
    public void showError() {
        Toast toast = Toast.makeText(getActivity(),
                R.string.string_weight_graph, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void generateDefaultData(List<Weight> weights) {

        boolean hasAxes = true;
        boolean hasAxesNames = true;
        boolean hasLabels = false;
        boolean hasLabelForSelected = false;

        int numSubcolumns = 1;

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;

        for (int i = 0; i < weights.size(); ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue(Float.parseFloat
                        (String.valueOf(weights.get(i).getCalories())), Color.rgb(105, 158, 58)));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM");

        for (int i = 0; i < weights.size(); ++i) {

            String label = mFormat.format(weights.get(i).getDate());
            axisValues.add(new AxisValue(i).setLabel(label));
        }

        ColumnChartData data = new ColumnChartData(columns);
        data.setStacked(true);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(" ");
                axisY.setName(" ");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        chart.setColumnChartData(data);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
        List<Weight> weights;

        private ValueTouchListener(List<Weight> weights) {
            this.weights = weights;
        }

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {

            SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM");
            String date = mFormat.format(weights.get(columnIndex).getDate());
            String calorii = weights.get(columnIndex).getCalories();
            Toast.makeText(getActivity(), getString(R.string.calorii) + " " + calorii + " " + getString(R.string.graph_date) + " " + date, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }
    }
}

