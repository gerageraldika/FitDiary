package com.carpediemsolution.fitdiary.ui.recyclerview;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Weight;


import java.util.List;

/**
 * Created by Юлия on 26.08.2017.
 */

public class FitAdapter  extends BaseAdapter<FitHolder, Weight> {

    public FitAdapter(@NonNull List<Weight> items) {
        super(items);
    }

    @Override
    public FitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FitHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FitHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Weight weight = getItem(position);
        holder.bind(weight);
    }

}