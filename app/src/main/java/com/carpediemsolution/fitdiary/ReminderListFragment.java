package com.carpediemsolution.fitdiary;
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
import com.carpediemsolution.fitdiary.database.CalculatorDbSchema;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.model.ReminderCounter;
import com.carpediemsolution.fitdiary.utils.CalculatorLab;
import com.carpediemsolution.fitdiary.utils.OnBackListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Юлия on 04.03.2017.
 */

public class ReminderListFragment extends Fragment implements OnBackListener {

private RecyclerView reminderRecyclerView;
 private ReminderAdapter reminderAdapter;
    List<Reminder> reminders;
    Reminder reminder;
    ReminderCounter reminderCounter;

   private  final String LOG_REMINDER ="ReminderListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_list_fragment,container, false);

        CalculatorLab calcLab = CalculatorLab.get(getActivity());
        reminders = calcLab.getReminds();
        CalculatorLab.get(getActivity()).getData();


        reminderRecyclerView = (RecyclerView)v.findViewById(R.id.reminder_recycler_view);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyTheme_Blue_Dialog); //alert for confirm to delete
                builder.setMessage(getString(R.string.sure_to_delete));    //set message

                builder.setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() { //when click on DELETE
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reminderAdapter.notifyItemRemoved(position);//item removed from recylcerview

                        reminder = reminders.get(position);
                        Log.d(LOG_REMINDER, "----" + "position " + reminders.get(position));
                        String uuidString = reminder.getUuid().toString();
                        CalculatorLab.get(getActivity()).mDatabase.delete(CalculatorDbSchema.CalculatorTable.NAME_REMEMBERING,
                                CalculatorDbSchema.CalculatorTable.Cols.REM_UUID + " = '" + uuidString + "'", null);
                        reminders.remove(position);  //then remove item
                        return;
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reminderAdapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                        reminderAdapter.notifyItemRangeChanged(position, reminderAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                        return;
                    }
                }).show();  //show alert dialog
            }
        }
    };

    private void updateUI() {
        reminderAdapter = new ReminderListFragment.ReminderAdapter();
        reminderRecyclerView.setAdapter(reminderAdapter);
        reminderAdapter.setReminds(reminders);
        reminderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {getActivity().finishAffinity();}

    private class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int EMPTY_VIEW = 5;
        private Reminder reminder;
        CalculatorLab calculatorLabLab = CalculatorLab.get(getActivity());
        List<Reminder> reminds = calculatorLabLab.getReminds();
        private TextView dateRemindTextView;
        private TextView notesRemindTextView;
        private CheckBox remindChecked;


        public class EmptyViewHolder extends RecyclerView.ViewHolder {
            public EmptyViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class ReminderHolder extends RecyclerView.ViewHolder  {

            public ReminderHolder(View itemView) {
                super(itemView);

                dateRemindTextView = (TextView) itemView.findViewById(R.id.reminder_date_item_text_view);
                notesRemindTextView = (TextView) itemView.findViewById(R.id.reminder_notes_item_text_view);
                remindChecked = (CheckBox) itemView.findViewById(R.id.remind_checked);

                remindChecked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getLayoutPosition();
                        reminder=reminders.get(position);
                        if(reminder.isCounter() == false) {

                            Log.d(LOG_REMINDER, "----" + "reminder " + reminder.isCounter());
                            reminder.setCounter(true);
                            reminderCounter = new ReminderCounter(reminder.getUuid());
                            reminderCounter.setCounterFlag(1);

                            Date date = reminder.getDate();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);

                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(year,month,day,0,0,0);
                            Date date1 = calendar1.getTime();

                            reminderCounter.setDate(date1);
                            Log.d(LOG_REMINDER, "----" + "date time " + date1.toString());
                                    CalculatorLab.get(getActivity()).addCounter(reminderCounter);
                            Log.d(LOG_REMINDER, "----" + "Add at first counter " +
                                    reminderCounter.getCounterFlag() + "---"+ reminderCounter.getDate() +"---"+ reminder.getUuid() +"---" +reminderCounter.getUuid());
                        }
                        else if (reminder.isCounter() == true){
                            reminder.setCounter(false);
                            Log.d(LOG_REMINDER, "----" + "хз reminder " + reminder.isCounter() );

                            List<ReminderCounter> reminderCounters = CalculatorLab.get(getActivity()).getRemindCounts();
                            for (int i = 0 ; i < reminderCounters.size(); i++){


                                if (reminderCounters.get(i).getUuid().toString().equals(reminder.getUuid().toString())){
                                    String uuidCounterString = reminderCounters.get(i).getUuid().toString();
                                    CalculatorLab.get(getActivity()).mDatabase.delete(CalculatorDbSchema.CalculatorTable.NAME_COUNTER,
                                            CalculatorDbSchema.CalculatorTable.Cols.COUNTER_UUID + " = '" + uuidCounterString + "'", null);
                                    Log.d(LOG_REMINDER, "----" + "string " + reminderCounters.get(i).getUuid());
                                }
                            }
                        }
                        CalculatorLab.get(getActivity()).updateReminder(reminder);
                        Log.d(LOG_REMINDER, "----" + "update reminder " +reminder.isCounter());
                    }
                });
                }
            }

        @Override
        public int getItemViewType(int position) {
            if (reminds.size() == 0) {
                return EMPTY_VIEW;
            } else {
                return super.getItemViewType(position);}
        }

        @Override
        public int getItemCount() {
            return reminds.size() > 0 ? reminds.size() : 1;
        }

        public void setReminds(List<Reminder> listReminds) {
            reminds = listReminds;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;

            if (viewType == EMPTY_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
                ReminderListFragment.ReminderAdapter.EmptyViewHolder evh = new ReminderListFragment.ReminderAdapter.EmptyViewHolder(v);
                // Log.d(LOG_TAG, "----" + "onCreateViewHolder" + mWeights.size());
                return evh;
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_recyclerview_item, parent, false);
                ReminderListFragment.ReminderAdapter.ReminderHolder vh = new ReminderListFragment.ReminderAdapter.ReminderHolder(v);
                return vh;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder vho, int position) {
            if (vho instanceof ReminderListFragment.ReminderAdapter.ReminderHolder) {
                ReminderListFragment.ReminderAdapter.ReminderHolder vh =
                        (ReminderListFragment.ReminderAdapter.ReminderHolder) vho;
                reminder = reminds.get(position);
                dateRemindTextView.setText(DateFormat.format("dd MM yyyy hh:mm", reminder.getDate()));
                notesRemindTextView.setText(reminder.getReminding());
                remindChecked.setChecked(reminder.isCounter());

                Log.d(LOG_REMINDER, "----" + "reminder " + reminder.isCounter());
            }
        }
    }
}



