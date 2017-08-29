package com.carpediemsolution.fitdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.activity.FitPagerActivity;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.presenter.FitPresenter;
import com.carpediemsolution.fitdiary.ui.recyclerview.BaseAdapter;
import com.carpediemsolution.fitdiary.ui.recyclerview.EmptyRecyclerView;
import com.carpediemsolution.fitdiary.ui.recyclerview.FitAdapter;
import com.carpediemsolution.fitdiary.util.OnBackListener;
import com.carpediemsolution.fitdiary.view.FitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Юлия on 11.02.2017.
 */

public class FitListFragment extends MvpAppCompatFragment implements FitView,
        BaseAdapter.OnItemClickListener<Weight>, OnBackListener {

    @InjectPresenter
    FitPresenter presenter;
    @BindView(R.id.weight_recycler_view)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty)
    View mEmptyView;
    private FitAdapter adapter;
    private static final String LOG_TAG = "FitListFragment";
    private static final String LOG_LF = "LifeCycle WeightsList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Log.d(LOG_LF, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fit_list, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        presenter.loadData();

        return view;
    }

    @Override
    public void onItemClick(@NonNull Weight item) {
        presenter.onItemClick(item);
    }

    @Override
    public void showSuccess() {
        //
    }

    @Override
    public void showError() {
        //toast error
    }

    @Override
    public void showDetails(@NonNull Weight weight) {
        Intent intent = FitPagerActivity.newIntent(getActivity(), weight.getId());
        startActivity(intent);
    }

    @Override
    public void showFitList(List<Weight> weightList) {
        adapter.changeDataSet(weightList);
        initSwipeRefreshLayout(weightList);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setEmptyView(mEmptyView);

        adapter = new FitAdapter(new ArrayList<>());
        adapter.attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
    }

    private void initSwipeRefreshLayout(List<Weight> weights) {
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
                            builder.setMessage(getString(R.string.sure_to_delete))    //set message
                                    .setPositiveButton(getString(R.string.remove), (dialog, which) -> {
                                        adapter.notifyItemRemoved(position);//item removed from recylcerview
                                        presenter.deleteWeight(weights.get(position));
                                        adapter.remove(weights.get(position));
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
    public void onBackPressed() {
        getActivity().finishAffinity();
    }

    //************************************************************************************
    // just for studying =)
    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_LF, "onResume");
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.d(LOG_LF, "onDestroyView");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_LF, "onAttach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_LF, "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_LF, "onActivityCreated");
        //очищаем стэк фрагментов
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(LOG_LF, "onViewStateRestored");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_LF, "onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_LF, "onSaveInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_LF, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_LF, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_LF, "onDetach");
    }
}


















