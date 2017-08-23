package com.carpediemsolution.fitdiary.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.model.RemindsCounter;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.utils.OnBackListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Юлия on 04.03.2017.
 */

public class RemindsListFragment extends Fragment implements OnBackListener {

    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminders;
    private Reminder reminder;
    private RemindsCounter reminderCounter;
    private FitLab sCalcLab;

    private final String LOG_REMINDER = "RemindsListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_list_fragment, container, false);

        sCalcLab = FitLab.get();
        reminders = sCalcLab.getReminds();

        reminderRecyclerView = (RecyclerView) v.findViewById(R.id.reminder_recycler_view);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(reminderRecyclerView); //set swipe to recylcerview

        return v;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition(); //get position which is swipe

            if (direction == ItemTouchHelper.LEFT) {    //if swipe left

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyTheme_Blue_Dialog); //alert for confirm to delete
                builder.setMessage(getString(R.string.sure_to_delete));    //set message

                builder.setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() { //when click on DELETE
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reminderAdapter.notifyItemRemoved(position);//item removed from recylcerview

                        reminder = reminders.get(position);
                        Log.d(LOG_REMINDER, "----" + "position " + reminders.get(position));
                        String uuidString = reminder.getUuid().toString();
                        sCalcLab.mDatabase.delete(DbSchema.CalculatorTable.NAME_REMEMBERING,
                                DbSchema.CalculatorTable.Cols.REM_UUID + " = '" + uuidString + "'", null);
                        reminders.remove(position);  //then remove item
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reminderAdapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                        reminderAdapter.notifyItemRangeChanged(position, reminderAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                    }
                }).show();  //show alert dialog
            }
        }
    };

    private void updateUI() {
        reminderAdapter = new RemindsListFragment.ReminderAdapter();
        reminderRecyclerView.setAdapter(reminderAdapter);
        reminderAdapter.setReminds(reminders);
        reminderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        getActivity().finishAffinity();
    }

    private class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int EMPTY_VIEW = 5;
        private Reminder reminder;
        List<Reminder> reminds = sCalcLab.getReminds();
        private TextView dateRemindTextView;
        private TextView notesRemindTextView;
        private CheckBox remindChecked;


        private class EmptyViewHolder extends RecyclerView.ViewHolder {
            private EmptyViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class ReminderHolder extends RecyclerView.ViewHolder {

            private ReminderHolder(View itemView) {
                super(itemView);

                dateRemindTextView = (TextView) itemView.findViewById(R.id.reminder_date_item_text_view);
                notesRemindTextView = (TextView) itemView.findViewById(R.id.reminder_notes_item_text_view);
                remindChecked = (CheckBox) itemView.findViewById(R.id.remind_checked);

                remindChecked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getLayoutPosition();
                        reminder = reminders.get(position);
                        if (!reminder.isCounter()) {

                            Log.d(LOG_REMINDER, "----" + "reminder " + reminder.isCounter());
                            reminder.setCounter(true);
                            reminderCounter = new RemindsCounter(reminder.getUuid());
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
                            Log.d(LOG_REMINDER, "----" + "date time " + date1.toString());
                            sCalcLab.addCounter(reminderCounter);
                            Log.d(LOG_REMINDER, "----" + "Add at first counter " +
                                    reminderCounter.getCounterFlag() + "---" + reminderCounter.getDate() + "---" + reminder.getUuid() + "---" + reminderCounter.getUuid());
                        } else if (reminder.isCounter()) {
                            reminder.setCounter(false);
                            Log.d(LOG_REMINDER, "----" + "хз reminder " + reminder.isCounter());

                            List<RemindsCounter> reminderCounters = sCalcLab.getRemindCounts();
                            for (int i = 0; i < reminderCounters.size(); i++) {


                                if (reminderCounters.get(i).getUuid().toString().equals(reminder.getUuid().toString())) {
                                    String uuidCounterString = reminderCounters.get(i).getUuid().toString();
                                    sCalcLab.mDatabase.delete(DbSchema.CalculatorTable.NAME_COUNTER,
                                            DbSchema.CalculatorTable.Cols.COUNTER_UUID + " = '" + uuidCounterString + "'", null);
                                    Log.d(LOG_REMINDER, "----" + "string " + reminderCounters.get(i).getUuid());
                                }
                            }
                        }
                        sCalcLab.updateReminder(reminder);
                        Log.d(LOG_REMINDER, "----" + "update reminder " + reminder.isCounter());
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (reminds.size() == 0) {
                return EMPTY_VIEW;
            } else {
                return super.getItemViewType(position);
            }
        }

        @Override
        public int getItemCount() {
            return reminds.size() > 0 ? reminds.size() : 1;
        }

        private void setReminds(List<Reminder> listReminds) {
            reminds = listReminds;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;

            if (viewType == EMPTY_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
                RemindsListFragment.ReminderAdapter.EmptyViewHolder evh = new RemindsListFragment.ReminderAdapter.EmptyViewHolder(v);
                // Log.d(LOG_TAG, "----" + "onCreateViewHolder" + mWeights.size());
                return evh;
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_recyclerview_item, parent, false);
                RemindsListFragment.ReminderAdapter.ReminderHolder vh = new RemindsListFragment.ReminderAdapter.ReminderHolder(v);
                return vh;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder vho, int position) {
            if (vho instanceof RemindsListFragment.ReminderAdapter.ReminderHolder) {
                RemindsListFragment.ReminderAdapter.ReminderHolder vh =
                        (RemindsListFragment.ReminderAdapter.ReminderHolder) vho;
                reminder = reminds.get(position);
                dateRemindTextView.setText(DateFormat.format("dd MM yyyy hh:mm", reminder.getDate()));
                notesRemindTextView.setText(reminder.getReminding());
                remindChecked.setChecked(reminder.isCounter());

                Log.d(LOG_REMINDER, "----" + "reminder " + reminder.isCounter());
            }
        }
    }
}



