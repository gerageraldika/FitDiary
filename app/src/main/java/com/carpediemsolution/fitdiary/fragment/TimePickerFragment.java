package com.carpediemsolution.fitdiary.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.carpediemsolution.fitdiary.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by Юлия on 03.03.2017.
 */

public class TimePickerFragment extends DialogFragment {
    private TimePicker timePicker;
    private Calendar calendar;

    private static final String ARG_TIME = "time";
    public static final String EXTRA_TIME = "com.carpediemsolution.fitdiary.time";

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment f = new TimePickerFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
       Date date = (Date)getArguments().getSerializable(ARG_TIME);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.time_picker, null);

       // Date time = (Date)getArguments().getSerializable(ARG_DATE);
        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = timePicker.getCurrentHour();
                                int min = timePicker.getCurrentMinute();
                                Date time = new GregorianCalendar(0, 0, 0, hour, min).getTime();
                                sendResult(Activity.RESULT_OK, time);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date time) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, time);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
