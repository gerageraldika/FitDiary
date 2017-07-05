package com.carpediemsolution.fitdiary.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.Weight;
import java.util.List;

/**
 * Created by Юлия on 25.05.2017.
 */

public class AsynkUtils {

    public static class WeightAverageTask extends AsyncTask<Void, Void, String> {
        TableLayout tabLayout;
        List<Weight>weights;
        Context context;

        public WeightAverageTask(TableLayout tabLayout, List<Weight>weights, Context context) {
            this.tabLayout = tabLayout;
            this.weights = weights;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            tabLayout.setStretchAllColumns(true);
            tabLayout.bringToFront();

            TableRow tableRowFirst = new TableRow(context);
            tableRowFirst.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            tableRowFirst.setBackgroundResource(R.color.colorDeepGreen);
            TextView c11 = new TextView(context);
            c11.setTextColor(context.getResources().getColor(R.color.colorWhite));
            c11.setText(context.getString(R.string.changing_weight_by_day));
           // c11.setTextSize(15);
            c11.setPadding(15, 20, 15, 20);
            tableRowFirst.addView(c11);
            tabLayout.addView(tableRowFirst);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... param) {
           return new MathUtils(context).getWeightProgress(weights);
        }
        @Override
        protected void onPostExecute(String string) {

            TableRow tableRowSecond = new TableRow(context);
            tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
            TextView c22 = new TextView(context);
            c22.setTextColor(context.getResources().getColor(R.color.colorWhite));
            c22.setText(string);
           // c22.setTextSize(15);
            c22.setPadding(15, 20, 15, 20);
            tableRowSecond.addView(c22);
            tabLayout.addView(tableRowSecond);
        }
    }

    public static class WeightStaticticTask extends AsyncTask<Void, Void, String> {
        TableLayout tabLayout;
        List<Weight>weights;
        Person person;
        Context context;

        public WeightStaticticTask(TableLayout tabLayout, List<Weight>weights, Person person,Context context) {
        this.tabLayout = tabLayout;
        this.weights = weights;
        this.person = person;
        this.context = context;
    }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
    protected String doInBackground(Void... param) {
        return new MathUtils(context).getWeightStatistics(weights,person);
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
        c11.setText(context.getString(R.string.average_weight));
       // c11.setTextSize(15);
        c11.setPadding(15, 20, 15, 20);
        tableRowFirst.addView(c11);

        TableRow tableRowSecond = new TableRow(context);
        tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
        TextView c22 = new TextView(context);
        c22.setTextColor(context.getResources().getColor(R.color.colorWhite));
        c22.setText(string);
      //  c22.setTextSize(15);
        c22.setPadding(15, 20, 15, 20);
        tableRowSecond.addView(c22);

        tabLayout.addView(tableRowFirst);
        tabLayout.addView(tableRowSecond);
    }
}

    public static class WeightResultTask extends AsyncTask<Void, Void, String> {
        TableLayout tabLayout;
        List<Weight>weights;
        Person person;
        Context context;

        public WeightResultTask(TableLayout tabLayout, List<Weight>weights, Person person,Context context) {
            this.tabLayout = tabLayout;
            this.weights = weights;
            this.person = person;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... param) {
            return new MathUtils(context).getWeightResults(weights,person);
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
           // c11.setTextSize(15);
            c11.setPadding(15, 20, 15, 20);
            tableRowFirst.addView(c11);

            TableRow tableRowSecond = new TableRow(context);
            tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
            TextView c22 = new TextView(context);
            c22.setTextColor(context.getResources().getColor(R.color.colorWhite));
            c22.setText(string);
           // c22.setTextSize(15);
            c22.setPadding(15, 20, 15, 20);
            tableRowSecond.addView(c22);

            tabLayout.addView(tableRowFirst);
            tabLayout.addView(tableRowSecond);
        }
    }

    public static class CaloriesStaticticTask extends AsyncTask<Void, Void, String> {
        TableLayout tabLayout;
        List<Weight>weights;
        Person person;
        Context context;

        public CaloriesStaticticTask(TableLayout tabLayout, List<Weight>weights, Context context) {
            this.tabLayout = tabLayout;
            this.weights = weights;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... param) {
            return new MathUtils(context).getAverageCalories(weights);
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
            c11.setText(context.getString(R.string.average_calories));
           // c11.setTextSize(15);
            c11.setPadding(15, 30, 15, 30);
            tableRowFirst.addView(c11);

            TableRow tableRowSecond = new TableRow(context);
            tableRowSecond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            tableRowSecond.setBackgroundResource(R.color.colorDeepGrey);
            TextView c22 = new TextView(context);
            c22.setTextColor(context.getResources().getColor(R.color.colorWhite));
            c22.setText(string);
            //c22.setTextSize(15);
            c22.setPadding(15, 20, 15, 20);

            tableRowSecond.addView(c22);

            tabLayout.addView(tableRowFirst);
            tabLayout.addView(tableRowSecond);
        }
    }
}
