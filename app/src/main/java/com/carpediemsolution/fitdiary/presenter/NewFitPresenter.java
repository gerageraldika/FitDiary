package com.carpediemsolution.fitdiary.presenter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.view.NewFitView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Юлия on 27.08.2017.
 */
@InjectViewState
public class NewFitPresenter extends MvpPresenter<NewFitView> {

    public void saveFit(String weight, String calories, String notes, Date date, String photoUri) {

        if (TextUtils.isEmpty(weight)) {
            getViewState().showError();

        } else if (Double.parseDouble(weight) <= 300) {
            Weight userWeight = new Weight(UUID.randomUUID());

            userWeight.setWeight(weight);
            if (!TextUtils.isEmpty(calories))
                userWeight.setCalories(calories);
            if (!TextUtils.isEmpty(notes))
                userWeight.setNotes(notes);
            userWeight.setDate(date);
            if (!TextUtils.isEmpty(photoUri))
                userWeight.setPhotoUri(photoUri);

            App.getFitLab().addWeight(userWeight);
            getViewState().showInfo(userWeight.getsWeight());

        } else if (Double.parseDouble(weight) > 300) {
            getViewState().showErrorMessage();
        }
    }

    public String getPhotoUri(Intent data) {
        //to presenter
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = App.getAppContext().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String photoUri = cursor.getString(columnIndex);
        cursor.close();
        return photoUri;
    }
}
