package com.carpediemsolution.fitdiary.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.model.RemindsCounter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Юлия on 27.08.2017.
 */

public class ReminderHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.reminder_date_item_text_view)
    TextView dateRemindTextView;
    @BindView(R.id.reminder_notes_item_text_view)
    TextView notesRemindTextView;
    @BindView(R.id.remind_checked)
    CheckBox remindChecked;

    public ReminderHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull Reminder reminder) {
        dateRemindTextView.setText(DateFormat.format("dd MM yyyy hh:mm", reminder.getDate()));
        notesRemindTextView.setText(reminder.getReminding());
        remindChecked.setChecked(reminder.isCounter());

        remindChecked.setOnClickListener((View v) -> {
            FitLab fitLab = App.getFitLab();

            if (!reminder.isCounter()) {
                //  Log.d(LOG_REMINDER, "----" + "reminder " + reminder.isCounter());
                reminder.setCounter(true);
                RemindsCounter reminderCounter = new RemindsCounter(reminder.getUuid());
                reminderCounter.setCounterFlag(1);

                Date date = reminder.getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(year, month, day, 0, 0, 0);
                Date date1 = calendar1.getTime();
                reminderCounter.setDate(date1);
                ///  Log.d(LOG_REMINDER, "----" + "date time " + date1.toString());
                fitLab.addCounter(reminderCounter);
                //  Log.d(LOG_REMINDER, "----" + "Add at first counter " + reminderCounter.getCounterFlag() + "---" + reminderCounter.getDate() + "---" + reminder.getUuid() + "---" + reminderCounter.getUuid());
            } else if (reminder.isCounter()) {
                reminder.setCounter(false);
                //  Log.d(LOG_REMINDER, "----" + "хз reminder " + reminder.isCounter());

                List<RemindsCounter> reminderCounters = fitLab.getRemindCounts();
                for (int i = 0; i < reminderCounters.size(); i++) {

                    if (reminderCounters.get(i).getUuid().toString().equals(reminder.getUuid().toString())) {
                        String uuidCounterString = reminderCounters.get(i).getUuid().toString();
                        fitLab.dataBase.delete(DbSchema.CalculatorTable.NAME_COUNTER,
                                DbSchema.CalculatorTable.Cols.COUNTER_UUID + " = '" + uuidCounterString + "'", null);
                        //   Log.d(LOG_REMINDER, "----" + "string " + reminderCounters.get(i).getUuid());
                    }
                }
            }
            fitLab.updateReminder(reminder);
            //  Log.d(LOG_REMINDER, "----" + "update reminder " + reminder.isCounter());
        });
    }
}




