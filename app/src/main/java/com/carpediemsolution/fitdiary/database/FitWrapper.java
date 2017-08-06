package com.carpediemsolution.fitdiary.database;

import android.database.Cursor;
import android.util.Log;

import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.model.RemindsCounter;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.database.DbSchema.CalculatorTable;

import java.util.Date;
import java.util.UUID;

public class FitWrapper extends android.database.CursorWrapper {
    //обертка для Cursor
    private static String LOG_TAG = "FitWrapper";

    public FitWrapper(Cursor cursor) {
        super(cursor);
    }

    public Weight getWeight() { //Cursor перебирается методом для получения объектов Weight
        String uuidString = getString(getColumnIndex(CalculatorTable.Cols.UUID));
        long date = getLong(getColumnIndex(CalculatorTable.Cols.DATE));
        String weight = getString(getColumnIndex(CalculatorTable.Cols.WEIGHT));
        String notes = getString(getColumnIndex(CalculatorTable.Cols.NOTES));
        String calories = getString(getColumnIndex(CalculatorTable.Cols.CALORIES));
        String photoUri = getString(getColumnIndex(CalculatorTable.Cols.PHOTOFILE));

        Weight weightDB = new Weight(UUID.fromString(uuidString));
        weightDB.setWeight(weight);
        weightDB.setDate(new Date(date));
        weightDB.setNotes(notes);
        weightDB.setPhotoUri(photoUri);
        weightDB.setCalories(calories);

        Log.d(LOG_TAG, "---- Get data from BD ---- " + weightDB.getPhotoUri());

        return weightDB;
    }

    public Reminder getReminder() {

        String id = getString(getColumnIndex(CalculatorTable.Cols.REM_UUID));
        long remDate = getLong(getColumnIndex(CalculatorTable.Cols.REM_DATE));
        String remNotes = getString(getColumnIndex(CalculatorTable.Cols.REM_NOTES));
        int remFlag = getInt(getColumnIndex(CalculatorTable.Cols.REM_FLAG));
        int remCounter = getInt(getColumnIndex(CalculatorTable.Cols.REM_COUNTER));

        Reminder reminderDB = new Reminder(UUID.fromString(id));
        reminderDB.setDate(new Date(remDate));
        reminderDB.setReminding(remNotes);
        reminderDB.setFlag(remFlag);
        reminderDB.setCounter(remCounter != 0);

        return reminderDB;
    }

    public RemindsCounter getReminderCounter() {

        String uuid = getString(getColumnIndex(CalculatorTable.Cols.COUNTER_UUID));
        long counterDate = getLong(getColumnIndex(CalculatorTable.Cols.COUNTER_DATE));
        int counterFlag = getInt(getColumnIndex(CalculatorTable.Cols.COUNTER_FLAG));

        RemindsCounter reminderCounterDB = new RemindsCounter(UUID.fromString(uuid));
        reminderCounterDB.setDate(new Date(counterDate));
        reminderCounterDB.setCounterFlag(counterFlag);

        return reminderCounterDB;
    }
}


