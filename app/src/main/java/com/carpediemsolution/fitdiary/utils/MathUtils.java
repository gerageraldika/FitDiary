package com.carpediemsolution.fitdiary.utils;

import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.Weight;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Юлия on 25.05.2017.
 */

public final class MathUtils {


    public static String personIMT(FitLab sCalcLab, double personWeight) {

        String greatIMT = App.getAppContext().getResources().getString(R.string.very_high_imt);
        String bigIMT = App.getAppContext().getResources().getString(R.string.high_imt);
        String normIMT = App.getAppContext().getResources().getString(R.string.norm_imt);
        String smallIMT = App.getAppContext().getString(R.string.small_imt);

        double personIMT = Math.round(personWeight / ((Math.pow((Double.parseDouble(sCalcLab.getPerson().getPersonHeight())), 2)) / 10000));

        if (personIMT >= 30) {
            return greatIMT + "\n" + App.getAppContext().getResources().getString(R.string.imt_message) + " " + String.format(Locale.US, "%.2f", personIMT);
        } else if (personIMT > 25 && personIMT < 30) {
            return bigIMT + "\n" + App.getAppContext().getResources().getString(R.string.imt_message) + " " + String.format(Locale.US, "%.2f", personIMT) + "\n";
        } else if (personIMT <= 25 && personIMT > 22) {
            return normIMT + "\n" + App.getAppContext().getResources().getString(R.string.imt_message) + " " + String.format(Locale.US, "%.2f", personIMT) + "\n";
        } else if (personIMT <= 22 && personIMT > 18.5) {
            return smallIMT + "\n" + App.getAppContext().getResources().getString(R.string.imt_message) + " " + String.format(Locale.US, "%.2f", personIMT) + "\n";
        } else
            return App.getAppContext().getResources().getString(R.string.imt_message) + "\n" + String.format(Locale.US, "%.2f", personIMT) + "\n";
    }


    public static String changingWeight(FitLab sCalcLab, double changedWeihgt) {

        double changedWeihgtMessage = (Double.parseDouble(sCalcLab.getPerson().getPersonWeight())) - changedWeihgt;
        if (changedWeihgtMessage < 0) {
            return App.getAppContext().getResources().getString(R.string.plus) + " " + String.format(Locale.US, "%.2f", (changedWeihgtMessage * (-1)));
        } else if (changedWeihgtMessage > 0) {
            return App.getAppContext().getResources().getString(R.string.minus) + " " + String.format(Locale.US, "%.2f", changedWeihgtMessage);
        } else return App.getAppContext().getResources().getString(R.string.no_changes);
    }


    private static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String getHoursOfDate() {
        Date date = new Date();

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        int hour = calendarDate.get(Calendar.HOUR_OF_DAY);

        if (hour > 3 && hour < 12) {
            return App.getAppContext().getResources().getString(R.string.good_morning);
        } else if (hour >= 12 && hour < 18) {
            return App.getAppContext().getResources().getString(R.string.good_day);
        } else if (hour >= 18 && hour < 24) {
            return App.getAppContext().getResources().getString(R.string.good_evening);
        } else return App.getAppContext().getResources().getString(R.string.good_night);
    }

    public static String getWeightProgress(List<Weight> weights) {
        int days = daysBetween(weights.get(weights.size() - 1).getDate(), weights.get(0).getDate());

        if (weights.size() > 0 && days != 0) {
            double weightProgress = Double.parseDouble(weights.get(weights.size() - 1).getsWeight())
                    - Double.parseDouble(weights.get(0).getsWeight());
            double average = weightProgress / days;
          return   String.format( Locale.US, "%.2f", average);
           // return String.valueOf(average);
        } else return "0";
    }

    public static String getWeightStatistics(List<Weight> weights, Person person) {
        double weightStatistics = Double.parseDouble(person.getPersonWeight());
       if(weights.size()> 0) {for (Weight weight : weights) {
            weightStatistics = weightStatistics + Double.parseDouble(weight.getsWeight());}
        double averageWeight = weightStatistics / (weights.size()+1);
        return String.format( Locale.US, "%.2f", averageWeight);}
        else return "0";
    }

    public static String getWeightResults(List<Weight> weights, Person person) {
       if(weights.size()> 0){
        Weight weight = weights.get(weights.size() - 1);
        double weightResult = Double.parseDouble(person.getPersonWeight()) - Double.parseDouble(weight.getsWeight());

        return String.format( Locale.US, "%.2f",weightResult);}
       else return "0";
    }

    public static String getAverageCalories(List<Weight> weights){
        double sumCalories = 0;
        int counter = 0;
        for (Weight weight : weights) {
            if(weight.getCalories()!= null){
            sumCalories = sumCalories + Double.parseDouble(weight.getCalories());
            counter++;}
        }
        double averageCalories = sumCalories / counter;
        return String.format( Locale.US, "%.2f",averageCalories);
    }
}
