package com.carpediemsolution.fitdiary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carpediemsolution.fitdiary.database.CalculatorDbSchema.CalculatorTable;

public class CalculatorBaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "CalculatorBaseHelper";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "calculatorBase.db";

    public CalculatorBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + CalculatorTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CalculatorTable.Cols.UUID + ", " +
                CalculatorTable.Cols.DATE + ", " +
                CalculatorTable.Cols.WEIGHT + ", " +
                CalculatorTable.Cols.NOTES + ", " +
                CalculatorTable.Cols.CALORIES + ", " +
                CalculatorTable.Cols.PHOTOFILE +
                ")"
        );

        db.execSQL("create table " + CalculatorTable.NAME_PERSON + "(" +
                CalculatorTable.Cols.ID +
                " integer primary key autoincrement, " +
                CalculatorTable.Cols.NAME + ", " +
                CalculatorTable.Cols.HEIGHT + ", " +
                CalculatorTable.Cols.PERSON_WEIGHT +
                ")"
        );

        db.execSQL("create table " + CalculatorTable.NAME_REMEMBERING + "(" +
                " _id integer primary key autoincrement, " +
                CalculatorTable.Cols.REM_UUID + ", " +
                CalculatorTable.Cols.REM_DATE + ", " +
                CalculatorTable.Cols.REM_NOTES + ", " +
                CalculatorTable.Cols.REM_COUNTER + ", " +
                CalculatorTable.Cols.REM_FLAG +
                ")"
        );

        db.execSQL("create table " + CalculatorTable.NAME_COUNTER + "(" +
                " _counter_id integer primary key autoincrement, " +
                CalculatorTable.Cols.COUNTER_UUID + ", " +
                CalculatorTable.Cols.COUNTER_DATE + ", " +
                CalculatorTable.Cols.COUNTER_FLAG +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

