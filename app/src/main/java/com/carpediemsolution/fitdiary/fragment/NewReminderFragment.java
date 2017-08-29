package com.carpediemsolution.fitdiary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.activity.PagerMainActivity;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.ui.dialogs.DatePickerFragment;
import com.carpediemsolution.fitdiary.ui.dialogs.TimePickerFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Юлия on 03.03.2017.
 */

public class NewReminderFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    private Reminder reminder;
    private Date date;
    private EditText notesInput;
    private CheckBox remindRepeat;
    private InterstitialAd interstitial;
    private List<Date> newDateList;
    private Date dateData;
    private Date timeData;
    private TextView dateInput;
    private TextView timeInput;
    private FitLab sCalcLab;
    private static final String CALENDAR_DIALOG_DATE = "CalendarDialogDate";
    private static final String CALENDAR_DIALOG_TIME = "CalendarDialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private String REMIND_LOG = "RemindLog";

    //to do ..
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        UUID id = UUID.randomUUID();
        reminder = new Reminder(id);
        sCalcLab = App.getFitLab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newDateList = new ArrayList<>();
        date = new Date();

        View view = inflater.inflate(R.layout.reminder_fragment, container, false);

        notesInput = (EditText) view.findViewById(R.id.notes_reminder);
        notesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reminder.setReminding(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dateInput = (TextView) view.findViewById(R.id.date_for_reminder);
        dateInput.setText(getString(R.string.date));
        timeInput = (TextView) view.findViewById(R.id.time_for_reminder);
        timeInput.setText(getString(R.string.time));
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment()
                        .newInstance(date);
                dialog.setTargetFragment(NewReminderFragment.this, REQUEST_DATE);
                dialog.show(fm, CALENDAR_DIALOG_DATE);
            }
        });

        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment()
                        .newInstance(date);
                dialog.setTargetFragment(NewReminderFragment.this, REQUEST_TIME);
                dialog.show(fm, CALENDAR_DIALOG_TIME);
            }
        });

        remindRepeat = (CheckBox) view.findViewById(R.id.remind_repeat);
        remindRepeat.setOnCheckedChangeListener(this);


        FloatingActionButton fabWriteReminding = (FloatingActionButton) view.findViewById(R.id.fab_reminding_write);
        fabWriteReminding.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_white_24dp));
        fabWriteReminding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                } else
                    onWriteRemind();
            }
        });

        interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId("ca-app-pub-9016583513972837/2464505107");

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                onWriteRemind();
            }
        });
        requestNewInterstitial();

        return view;
    }

    public void onWriteRemind() {
        if (reminder.getDate() == null) {
            Toast toast = Toast.makeText(getActivity(),
                    getString(R.string.insert_date_and_time), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (reminder.getReminding() != null && reminder.getDate() != null) {
            reminder.setCounter(false);
            sCalcLab.addReminder(reminder);

            sCalcLab.createRepeatedRemind();
            Log.d(REMIND_LOG, "---reminder" + reminder.getFlag());
            // FitLab.get(getActivity());
            Intent intent = new Intent(getActivity(), PagerMainActivity.class);
            startActivity(intent);

        } else if (reminder.getReminding() == null) {
            Toast toast = Toast.makeText(getActivity(),
                    getString(R.string.insert_reminding), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Date newdate;
        if (requestCode == REQUEST_DATE) {
            dateData = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            newDateList.add(dateData);
            newdate = newDate(newDateList);
            dateInput.setText(DateFormat.format("dd MM yyyy", dateData));
            reminder.setDate(newdate);
            Log.d(REMIND_LOG, "----date" + dateData + "---newdate" + newdate);

        } else if (requestCode == REQUEST_TIME) {
            timeData = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            newDateList.add(timeData);
            newdate = newDate(newDateList);
            timeInput.setText(DateFormat.format("HH:mm", timeData));
            reminder.setDate(newdate);
            Log.d(REMIND_LOG, "----time" + timeData + newDateList + "---newdate" + newdate);
        }
    }

    public Date newDate(List<Date> newDate) {

        Date remindDate = new Date();

        if ((dateData == null) && (timeData == null)) {
            return remindDate = null;
        }

        if ((dateData != null) && (timeData != null) && (newDate.size() == 2)) {

            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(dateData);
            int year = calendarDate.get(Calendar.YEAR);
            int month = calendarDate.get(Calendar.MONTH);
            int day = calendarDate.get(Calendar.DAY_OF_MONTH);

            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTime(timeData);

            int hour = calendarTime.get(Calendar.HOUR_OF_DAY);
            int min = calendarTime.get(Calendar.MINUTE);

            remindDate = new GregorianCalendar(year, month, day, hour, min).getTime();
        }
        Log.d(REMIND_LOG, "----list metod" + remindDate);

        if (newDate.size() > 2) {
            newDate.remove(0);
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(dateData);
            int year = calendarDate.get(Calendar.YEAR);
            int month = calendarDate.get(Calendar.MONTH);
            int day = calendarDate.get(Calendar.DAY_OF_MONTH);

            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTime(timeData);

            int hour = calendarTime.get(Calendar.HOUR_OF_DAY);
            int min = calendarTime.get(Calendar.MINUTE);

            remindDate = new GregorianCalendar(year, month, day, hour, min).getTime();
            Log.d(REMIND_LOG, "----list metod" + remindDate);
        }
        if ((dateData != null) && (timeData == null) && (newDate.size() == 1)) {
            remindDate = dateData;
        }
        if ((dateData == null) && (timeData != null) && (newDate.size() == 1)) {
            remindDate = timeData;
        }
        return remindDate;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("3EED2099D69A864B")
                .build();
        interstitial.loadAd(adRequest);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            reminder.setFlag(1);
        } else {
            reminder.setFlag(0);
        }

        sCalcLab.getData();
        Log.d(REMIND_LOG, "----" + "update remind " +
                reminder.getFlag());
    }
}







