package com.carpediemsolution.fitdiary.charts.presenters;

import android.os.AsyncTask;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.charts.views.ReminderChartView;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.RemindsCounter;
import com.carpediemsolution.fitdiary.model.Weight;

import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Юлия on 28.08.2017.
 */
@InjectViewState
public class ReminderChartPresenter extends MvpPresenter<ReminderChartView> {
    //init presenter
    private void initAsync(List<Weight> weightList, Person person) {
        new CaloriesAsync(weightList).execute();
        new WeightAverageAsync(weightList).execute();
        new WeightStatisticAsync(weightList, person).execute();
        new WeightResultAsync(weightList, person).execute();
    }

    public void init(){
        FitLab fitLab = App.getFitLab();
        if (!DbSchema.CalculatorTable.NAME_COUNTER.isEmpty() &&
                fitLab.getRemindCounts().size() > 0) {

            List<RemindsCounter> reminderCounters = fitLab.getRemindCounts();
            getViewState().showReminderStatistic(reminderCounters);
        }

        if (!DbSchema.CalculatorTable.NAME.isEmpty() && fitLab.getWeights().size() > 0) {
            List<Weight> weights = fitLab.getWeights();
            Person person = fitLab.getPerson();
            initAsync(weights, person);
        } else {
           getViewState().showError();
        }
    }

    private class CaloriesAsync extends AsyncTask<Void, Void, String> {
        private List<Weight> weights;

        private CaloriesAsync(List<Weight> weights) {
            this.weights = weights;
        }

        @Override
        protected String doInBackground(Void... param) {
            return getAverageCalories(weights);
        }

        @Override
        protected void onPostExecute(String string) {
            getViewState().showCalories(string);
        }
    }

    private class WeightAverageAsync extends AsyncTask<Void, Void, String> {
        private List<Weight> weights;

        private WeightAverageAsync(List<Weight> weights) {
            this.weights = weights;
        }

        @Override
        protected void onPreExecute() {
            getViewState().beforeWeightAverageAsync();
        }

        @Override
        protected String doInBackground(Void... param) {
            return getWeightProgress(weights);
        }

        @Override
        protected void onPostExecute(String string) {
            getViewState().showWeightAverage(string);
        }
    }

    private class WeightStatisticAsync extends AsyncTask<Void, Void, String> {
        private List<Weight> weights;
        private Person person;

        private WeightStatisticAsync(List<Weight> weights, Person person) {
            this.weights = weights;
            this.person = person;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... param) {
            return getWeightStatistics(weights, person);

        }

        @Override
        protected void onPostExecute(String string) {
            getViewState().showWeightStatistic(string);
        }
    }

    private class WeightResultAsync extends AsyncTask<Void, Void, String> {
        private List<Weight> weights;
        private Person person;

        private WeightResultAsync(List<Weight> weights, Person person) {
            this.weights = weights;
            this.person = person;
        }

        @Override
        protected String doInBackground(Void... param) {
            return getWeightResults(weights, person);
        }

        @Override
        protected void onPostExecute(String string) {
            getViewState().showWeightResults(string);
        }
    }


    private String getAverageCalories(List<Weight> weights) {
        double sumCalories = 0;
        int counter = 0;
        for (Weight weight : weights) {
            if (weight.getCalories() != null) {
                sumCalories = sumCalories + Double.parseDouble(weight.getCalories());
                counter++;
            }
        }
        double averageCalories = sumCalories / counter;
        return String.format(Locale.US, "%.2f", averageCalories);
    }

    private String getWeightProgress(List<Weight> weights) {
        int days = daysBetween(weights.get(weights.size() - 1).getDate(), weights.get(0).getDate());

        if (weights.size() > 0 && days != 0) {
            double weightProgress = Double.parseDouble(weights.get(weights.size() - 1).getsWeight())
                    - Double.parseDouble(weights.get(0).getsWeight());
            double average = weightProgress / days;
            return String.format(Locale.US, "%.2f", average);
            // return String.valueOf(average);
        } else return "0";
    }

    private int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    private String getWeightStatistics(List<Weight> weights, Person person) {
        double weightStatistics = Double.parseDouble(person.getPersonWeight());
        if (weights.size() > 0) {
            for (Weight weight : weights) {
                weightStatistics = weightStatistics + Double.parseDouble(weight.getsWeight());
            }
            double averageWeight = weightStatistics / (weights.size() + 1);
            return String.format(Locale.US, "%.2f", averageWeight);
        } else return "0";
    }

    private String getWeightResults(List<Weight> weights, Person person) {
        if (weights.size() > 0) {
            Weight weight = weights.get(weights.size() - 1);
            double weightResult = Double.parseDouble(person.getPersonWeight()) - Double.parseDouble(weight.getsWeight());

            return String.format(Locale.US, "%.2f", weightResult);
        } else return "0";
    }

        /*  in case of rx:
        Func1<List<Weight>, String> listToString = new Func1<List<Weight>, String>() {
        @Override
        public String call(List<Weight> weights) {
            return  getAverageCalories(weights);
        }
    };*/
}

