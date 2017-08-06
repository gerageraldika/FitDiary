package com.carpediemsolution.fitdiary.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.database.CalculatorBaseHelper;
import com.carpediemsolution.fitdiary.database.CalculatorCursorWrapper;
import com.carpediemsolution.fitdiary.database.CalculatorDbSchema.CalculatorTable;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.model.ReminderCounter;
import com.carpediemsolution.fitdiary.model.Weight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Юлия on 09.02.2017.
 */

public class CalculatorLab {
    private static CalculatorLab calcLab;

    public SQLiteDatabase mDatabase;
    private static String LAB_LOG = "LabLog";

    public static CalculatorLab get() {
        if (calcLab == null) {
            calcLab = new CalculatorLab();}
        return calcLab;}

    private CalculatorLab() {
        mDatabase = new CalculatorBaseHelper(App.getAppContext())
                .getWritableDatabase();}

    public void addWeight(Weight c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CalculatorTable.NAME, null, values);}
        //Log.d(LAB_LOG, "---- addWeight----" + values + mDatabase.toString());}

    public List<Weight> getWeights() {
        List<Weight> weights = new ArrayList<>();
        CalculatorCursorWrapper cursor = queryCalculator(null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            weights.add(cursor.getWeight());
            cursor.moveToNext();
        }
        Collections.sort(weights, new Comparator<Weight>() {
            @Override
            public int compare(Weight o1, Weight o2) {
                return o1.getDate().compareTo(o2.getDate());}
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        Collections.reverse(weights);
        //
        return weights;
    }

    public Weight getWeight(UUID id) {
        CalculatorCursorWrapper cursor = queryCalculator(
                CalculatorTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getWeight();
        } finally {
            cursor.close();}
    }

    private static ContentValues getContentValues(Weight weight) {
        ContentValues values = new ContentValues();
        values.put(CalculatorTable.Cols.UUID, weight.getId().toString());
        values.put(CalculatorTable.Cols.DATE, weight.getDate().getTime());
        values.put(CalculatorTable.Cols.WEIGHT, weight.getsWeight());
        values.put(CalculatorTable.Cols.NOTES, weight.getNotes());
        values.put(CalculatorTable.Cols.CALORIES, weight.getCalories());
        values.put(CalculatorTable.Cols.PHOTOFILE, weight.getPhotoUri());
        return values;}

    private CalculatorCursorWrapper queryCalculator(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CalculatorTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new CalculatorCursorWrapper(cursor);}

    public void updateWeight(Weight weight) {
        String uuidString = weight.getId().toString();
        ContentValues values = getContentValues(weight);

        mDatabase.update(CalculatorTable.NAME, values,
                CalculatorTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getPersonContentValues(Person person) {
        ContentValues values = new ContentValues();
        values.put(CalculatorTable.Cols.ID, "1");
        values.put(CalculatorTable.Cols.NAME, person.getPersonName());
        values.put(CalculatorTable.Cols.HEIGHT, person.getPersonHeight());
        values.put(CalculatorTable.Cols.PERSON_WEIGHT, person.getPersonWeight());
        return values;}

    public void updatePerson(Person person) {
        ContentValues values = getPersonContentValues(person);
        mDatabase.update(CalculatorTable.NAME_PERSON, values,
                CalculatorTable.Cols.ID + " = ?", new String[]{"1"});
        Log.d(LAB_LOG, "----" + "updateDBPerson" + "----" + person.getPersonName() + getPerson().getPersonName()
        );
    }

    public void addPerson(Person person) {
        ContentValues values = getPersonContentValues(person);
        mDatabase.insert(CalculatorTable.NAME_PERSON, null, values);
        Log.d(LAB_LOG, "----" + "addPerson" + "----" + CalculatorTable.NAME_PERSON);}

    public Person getPerson() {
        String[] projection = {
                CalculatorTable.Cols.ID,
                CalculatorTable.Cols.NAME,
                CalculatorTable.Cols.HEIGHT,
                CalculatorTable.Cols.PERSON_WEIGHT,
        };
        Cursor cursor = mDatabase.query(
                CalculatorTable.NAME_PERSON,  // The table to query
                projection, // Columns - null selects all columns
                null,
                null,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
           // String id = "1";
            String name = cursor.getString(cursor.getColumnIndex(CalculatorTable.Cols.NAME));
            String height = cursor.getString(cursor.getColumnIndex(CalculatorTable.Cols.HEIGHT));
            String weight = cursor.getString(cursor.getColumnIndex(CalculatorTable.Cols.PERSON_WEIGHT));

            Person personDB = Person.get();
            personDB.setPersonHeight(height);
            personDB.setPersonName(name);
            personDB.setPersonWeight(weight);
            return personDB;
        } finally {
            cursor.close();}
    }

    public boolean returnPerson() {
        boolean empty;
        String[] projection = {
                CalculatorTable.Cols.ID,
                CalculatorTable.Cols.NAME,
                CalculatorTable.Cols.HEIGHT,
                CalculatorTable.Cols.PERSON_WEIGHT,
        };
        Cursor cursor = mDatabase.query(
                CalculatorTable.NAME_PERSON,  // The table to query
                projection, // Columns - null selects all columns
                null,
                null,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        if ((cursor != null) && (cursor.getCount() > 0)) {
            empty = true;
            cursor.close();
        } else {
            empty = false;
        }

        return empty;}

    private static ContentValues getReminderContentValues(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(CalculatorTable.Cols.REM_UUID, reminder.getUuid().toString());
        values.put(CalculatorTable.Cols.REM_DATE, reminder.getDate().getTime());
        values.put(CalculatorTable.Cols.REM_NOTES, reminder.getReminding());
        values.put(CalculatorTable.Cols.REM_COUNTER, reminder.isCounter() ? 1 : 0);
        values.put(CalculatorTable.Cols.REM_FLAG, reminder.getFlag());
        return values;}

    public void addReminder(Reminder reminder) {
        ContentValues values = getReminderContentValues(reminder);
        mDatabase.insert(CalculatorTable.NAME_REMEMBERING, null, values);
        Log.d(LAB_LOG, "----" + "addReminder" + "----" + CalculatorTable.NAME_REMEMBERING);}

    public void updateReminder(Reminder reminder) {
        String uuidString = reminder.getUuid().toString();
        ContentValues values = getReminderContentValues(reminder);

        mDatabase.update(CalculatorTable.NAME_REMEMBERING, values,
                CalculatorTable.Cols.REM_UUID + " = ?",
                new String[]{uuidString});
        Log.d(LAB_LOG, "----" + "updateReminder" + reminder.getFlag() + "----");}

    public void createRepeatedRemind() {
        List<Reminder> reminders = getReminds();
        Date date = new Date();

        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(date);
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            Calendar reminderDate = Calendar.getInstance();
            reminderDate.setTime(reminder.getDate());
            int reminderYear = currentDate.get(Calendar.YEAR);
            int reminderMonth = currentDate.get(Calendar.MONTH);
            int reminderDay = reminderDate.get(Calendar.DAY_OF_MONTH);
            int reminderHour = reminderDate.get(Calendar.HOUR_OF_DAY);
            int reminderMin = reminderDate.get(Calendar.MINUTE);

            Log.d(LAB_LOG, "createRepeatedRemind " + day + "----" + reminderDay);
            if ((reminder.getFlag() == 1) && (day > reminderDay) && (year == reminderYear) && (month == reminderMonth)) {

                Date newDate = new GregorianCalendar(year, month, day, reminderHour, reminderMin).getTime();

                Reminder reminderRepeated = new Reminder();
                reminderRepeated.setDate(newDate);
                reminderRepeated.setReminding(reminder.getReminding());
                reminderRepeated.setFlag(1);
                reminder.setFlag(2);
                updateReminder(reminder);
                addReminder(reminderRepeated);
                Log.d(LAB_LOG, "createRepeatedRemind " + reminderRepeated.getDate() + "----" + reminder.getDate());
            }
        }
    }

    public ReminderCounter getReminderCount() {
        String[] projection = {
                CalculatorTable.Cols.COUNTER_UUID,
                CalculatorTable.Cols.COUNTER_DATE,
                CalculatorTable.Cols.COUNTER_FLAG,
        };
        Cursor cursor = mDatabase.query(
                CalculatorTable.NAME_COUNTER,  // The table to query
                projection, // Columns - null selects all columns
                null,
                null,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            String uuid = cursor.getString(cursor.getColumnIndex(CalculatorTable.Cols.COUNTER_UUID));
            long counterDate = cursor.getLong(cursor.getColumnIndex(CalculatorTable.Cols.COUNTER_DATE));
            int counterFlag = cursor.getInt(cursor.getColumnIndex(CalculatorTable.Cols.COUNTER_FLAG));

            ReminderCounter remindercounterDB = new ReminderCounter(UUID.fromString(uuid));
            remindercounterDB.setDate(new Date(counterDate));
            remindercounterDB.setCounterFlag(counterFlag);
            return remindercounterDB;

        } finally {
            cursor.close();
        }
    }


    private CalculatorCursorWrapper queryReminder(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CalculatorTable.NAME_REMEMBERING,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                CalculatorTable.Cols.REM_DATE + " DESC"  // orderBy
        );
        return new CalculatorCursorWrapper(cursor);}

    public List<Reminder> getReminds() {
        List<Reminder> reminds = new ArrayList<>();

        CalculatorCursorWrapper cursor = queryReminder(null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            reminds.add(cursor.getReminder());
            cursor.moveToNext();
        }
        cursor.close();
        return reminds;}

    private CalculatorCursorWrapper queryReminderCount(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CalculatorTable.NAME_COUNTER,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                CalculatorTable.Cols.COUNTER_DATE + " ASC"   // orderBy
        );

        return new CalculatorCursorWrapper(cursor);
    }

    public List<ReminderCounter> getRemindCounts() {
        List<ReminderCounter> reminderCounters = new ArrayList<>();

        CalculatorCursorWrapper cursor = queryReminderCount(null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            reminderCounters.add(cursor.getReminderCounter());
            cursor.moveToNext();
        }
        cursor.close();

       Collections.sort(reminderCounters, new Comparator<ReminderCounter>() {
            @Override
            public int compare(ReminderCounter o1, ReminderCounter o2) {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });

        return reminderCounters;
    }

    private static ContentValues getCounterContentValues(ReminderCounter reminderCounter) {
        ContentValues values = new ContentValues();
        //values.put(CalculatorTable.Cols.ID, );
        values.put(CalculatorTable.Cols.COUNTER_UUID, reminderCounter.getUuid().toString());
        values.put(CalculatorTable.Cols.COUNTER_DATE, reminderCounter.getDate().getTime());
        values.put(CalculatorTable.Cols.COUNTER_FLAG, reminderCounter.getCounterFlag());
        return values;
    }

    private ReminderCounter getReminderCount(String date) {
        CalculatorCursorWrapper cursor = queryReminderCount(
                CalculatorTable.Cols.COUNTER_DATE + " = ?",
                new String[]{date.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getReminderCounter();
        } finally {
            cursor.close();
        }
    }

    public void addCounter(ReminderCounter reminderCounter) {
        try {
            String dateString = reminderCounter.getDate().toString();

            ReminderCounter reminderCounter1 =
                    getReminderCount(reminderCounter.getDate().toString());
            int count = reminderCounter1.getCounterFlag() + 1;
            reminderCounter1.setCounterFlag(count);

            ContentValues values = getCounterContentValues(reminderCounter1);
            mDatabase.update(CalculatorTable.NAME_COUNTER, values,
                    CalculatorTable.Cols.COUNTER_DATE + " = ?",
                    new String[]{dateString});
            Log.d(LAB_LOG, "----" + "addCounter" + "----" + reminderCounter.getCounterFlag() +
                    "--" + reminderCounter.getDate());

        } catch (NullPointerException e) {
            ContentValues values = getCounterContentValues(reminderCounter);
            mDatabase.insert(CalculatorTable.NAME_COUNTER, null, values);
            Log.d(LAB_LOG, "----" + "addCounter 1" + "----" + reminderCounter.getCounterFlag() +
                    "--" + reminderCounter.getDate());
        }


    }

    public void updateRemindCounter(ReminderCounter reminderCounter) {
        String uuidString = reminderCounter.getUuid().toString();
        ContentValues values = getCounterContentValues(reminderCounter);

        mDatabase.update(CalculatorTable.NAME_COUNTER, values,
                CalculatorTable.Cols.COUNTER_UUID + " = ?",
                new String[]{uuidString});}

    public String getData() {
        String[] columns = {CalculatorTable.Cols.COUNTER_ID, CalculatorTable.Cols.COUNTER_UUID,
                CalculatorTable.Cols.COUNTER_DATE,
                CalculatorTable.Cols.COUNTER_FLAG};
        Cursor cursor = mDatabase.query(CalculatorTable.NAME_COUNTER, columns, null, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            Long date = cursor.getLong(2);
            Date newDate = new Date(date);
            int counter = cursor.getInt(3);
            buffer.append("\n" + newDate + "\n" + counter + "\n");
        }
        cursor.close();
        Log.d(LAB_LOG, "----" + "ListCounter" + "----" + buffer.toString());
        return buffer.toString();}
}











