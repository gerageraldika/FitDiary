package com.carpediemsolution.fitdiary.view;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Reminder;

import java.util.List;

/**
 * Created by Юлия on 27.08.2017.
 */

public interface ReminderView extends MvpView, BaseView{

    void showRemindsList(@NonNull List<Reminder> reminderList);
}
