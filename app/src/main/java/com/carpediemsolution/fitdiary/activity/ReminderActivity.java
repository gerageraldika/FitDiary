package com.carpediemsolution.fitdiary.activity;


import android.support.v4.app.Fragment;

import com.carpediemsolution.fitdiary.ui.adapter.SingleFragmentActivity;
import com.carpediemsolution.fitdiary.fragment.NewReminderFragment;

/**
 * Created by Юлия on 03.03.2017.
 */

public class ReminderActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NewReminderFragment();
    }
}

