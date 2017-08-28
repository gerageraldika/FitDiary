package com.carpediemsolution.fitdiary.ui.recyclerview;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Reminder;

import java.util.List;

/**
 * Created by Юлия on 27.08.2017.
 */

public class ReminderAdapter extends BaseAdapter<ReminderHolder, Reminder> {

    public ReminderAdapter(@NonNull List<Reminder> items) {
        super(items);
    }

    @Override
    public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReminderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ReminderHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Reminder reminder = getItem(position);
        holder.bind(reminder);
    }
}