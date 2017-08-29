package com.carpediemsolution.fitdiary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Reminder;
import com.carpediemsolution.fitdiary.presenter.ReminderPresenter;
import com.carpediemsolution.fitdiary.ui.recyclerview.BaseAdapter;
import com.carpediemsolution.fitdiary.ui.recyclerview.EmptyRecyclerView;
import com.carpediemsolution.fitdiary.ui.recyclerview.ReminderAdapter;
import com.carpediemsolution.fitdiary.util.OnBackListener;
import com.carpediemsolution.fitdiary.view.ReminderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Юлия on 04.03.2017.
 */

public class ReminderListFragment extends MvpAppCompatFragment implements ReminderView,
        BaseAdapter.OnItemClickListener<Reminder>, OnBackListener {

    @InjectPresenter
    ReminderPresenter presenter;
    @BindView(R.id.reminder_recycler_view)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty)
    View mEmptyView;
    private ReminderAdapter adapter;
    // private final String LOG_REMINDER = "ReminderListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_list_fragment, container, false);
        ButterKnife.bind(this, v);

        initRecyclerView();
        presenter.loadData();

        return v;
    }

    @Override
    public void onBackPressed() {
        getActivity().finishAffinity();
    }

    @Override
    public void showSuccess() {
        //loading dialog
    }

    @Override
    public void showError() {
        //toast
    }

    @Override
    public void showRemindsList(@NonNull List<Reminder> reminderList) {

        adapter.changeDataSet(reminderList);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
                            builder.setPositiveButton(getString(R.string.remove), (dialog, which) -> {
                                adapter.notifyItemRemoved(position);//item removed from recylcerview
                                presenter.deleteReminder(reminderList.get(position));
                                adapter.remove(reminderList.get(position));
                                adapter.refreshRecycler();
                            })
                                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                                        adapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                                    })
                                    .show();
                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview
    }

    @Override
    public void onItemClick(@NonNull Reminder item) {
        //to do
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setEmptyView(mEmptyView);
        adapter = new ReminderAdapter(new ArrayList<>());
        adapter.attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
    }
}




