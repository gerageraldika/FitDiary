package com.carpediemsolution.fitdiary.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.view.ReminderView;

/**
 * Created by Юлия on 27.08.2017.
 */
@InjectViewState
public class ReminderPresenter extends MvpPresenter<ReminderView> {

    public void loadData(){
        //db
        try {
            getViewState().showRemindsList(App.getFitLab().getReminds());
        }
        catch (Exception e){
            getViewState().showError();
        }
    }

    public void deleteReminder(Reminder reminder){
        String uuidString = reminder.getUuid().toString();
        App.getFitLab().dataBase.delete(DbSchema.CalculatorTable.NAME_REMEMBERING,
                DbSchema.CalculatorTable.Cols.REM_UUID + " = '" + uuidString + "'", null);
    }
}
