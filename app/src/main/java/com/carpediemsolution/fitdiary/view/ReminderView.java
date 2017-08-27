package com.carpediemsolution.fitdiary.view;

import com.arellomobile.mvp.MvpView;
import com.carpediemsolution.fitdiary.model.Reminder;

import java.util.List;

/**
 * Created by Юлия on 27.08.2017.
 */

public interface ReminderView extends MvpView, BaseView{

    void showRemindsList(List<Reminder> reminderList);
}
