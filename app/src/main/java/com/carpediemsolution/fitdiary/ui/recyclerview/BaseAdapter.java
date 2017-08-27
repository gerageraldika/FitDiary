package com.carpediemsolution.fitdiary.ui.recyclerview;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Юлия on 18.08.2017.
 */

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    private final List<T> mItems = new ArrayList<>();

    @Nullable
    private OnItemClickListener<T> mOnItemClickListener;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    private final View.OnClickListener mInternalListener = (view) -> {
        if (mOnItemClickListener != null) {
            int position = (int) view.getTag();
            T item = mItems.get(position);
            mOnItemClickListener.onItemClick(item);
        }
    };

    @Nullable
    private EmptyRecyclerView mRecyclerView;

    public BaseAdapter(@NonNull List<T> items) {
        mItems.addAll(items);
    }

    public void attachToRecyclerView(@NonNull EmptyRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setAdapter(this);
        refreshRecycler();
    }

    public final void changeDataSet(@NonNull List<T> values) {
        mItems.clear();
        mItems.addAll(values);
        refreshRecycler();
    }

   public final void clear() {
        mItems.clear();
        refreshRecycler();
    }


    public void refreshRecycler() {
        notifyDataSetChanged();
        if (mRecyclerView != null) {
            mRecyclerView.checkIfEmpty();
        }
    }

    private void add(T item) {

        mItems.add(item);
        // notifyItemInserted(cards.size() - 1);
        notifyItemInserted(mItems.size());
    }

    public void addAll(List<T> items) {
        mItems.clear();
        for (T item : items) {
            add(item);
        }
    }

    public void remove(T item) {
        int position = mItems.indexOf(item);
        if (position > -1) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    @CallSuper
    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mItems.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public interface OnItemClickListener<T> {

        void onItemClick(@NonNull T item);

    }

}
