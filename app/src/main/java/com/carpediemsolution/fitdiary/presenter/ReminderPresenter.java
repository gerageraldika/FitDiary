package com.carpediemsolution.fitdiary.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.view.ReminderView;

import java.util.List;

/**
 * Created by Юлия on 27.08.2017.
 */
@InjectViewState
public class ReminderPresenter extends MvpPresenter<ReminderView> {

    public void loadData(){
        //db
        try {
            getViewState().showRemindsList(FitLab.get().getReminds());
        }
        catch (Exception e){
            getViewState().showError();
        }
    }

    public void deleteReminder(Reminder reminder){
        String uuidString = reminder.getUuid().toString();
        FitLab.get().mDatabase.delete(DbSchema.CalculatorTable.NAME_REMEMBERING,
                DbSchema.CalculatorTable.Cols.REM_UUID + " = '" + uuidString + "'", null);
    }

    public void init(List<Weight> weights, Person person) {

    }
}
