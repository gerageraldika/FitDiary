package com.carpediemsolution.fitdiary.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.util.async.PhotoItemAsync;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Юлия on 18.08.2017.
 */
public class FitHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_item_weight_title_text_view)
    TextView mTitleTextView;
    @BindView(R.id.list_item__date_text_view)
    TextView mDateTextView;
    @BindView(R.id.list_item_photo)
    ImageView mPhotoView;
    @BindView(R.id.list_item__imt_text_view)
    TextView mIMTView;
    @BindView(R.id.list_item__calories_text_view)
    TextView mCaloriesTextView;

    public FitHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull Weight weight) {
        Person person = App.getFitLab().getPerson();

        mDateTextView.setText(DateFormat.format("dd MM yyyy", weight.getDate()));
        mTitleTextView.setText((App.getAppContext().getString(R.string.weight)) + " " + weight.getsWeight() + " "
                + ((App.getAppContext().getString(R.string.kg))));
        if (weight.getCalories() != null) {
            mCaloriesTextView.setText(((App.getAppContext().getString(R.string.calorii))
                    + " " + weight.getCalories()));
        }
        mPhotoView.setImageResource(R.drawable.ic_perm_identity_white_24dp);

        double personIMT = Math.round(Double.parseDouble(weight.getsWeight()) /
                ((Math.pow((Double.parseDouble(person.getPersonHeight())), 2)) / 10000));
        String personIMTtoday = String.valueOf(personIMT);
        mIMTView.setText("  /  " + ((App.getAppContext().getString(R.string.imt)) + " " + personIMTtoday));

        PhotoItemAsync photoItemsTask = new PhotoItemAsync(mPhotoView, weight);
        photoItemsTask.execute();
    }
}



