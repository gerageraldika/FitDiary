package com.carpediemsolution.fitdiary.ui_utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.activity.PagerMainActivity;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.dao.CalculatorLab;
import com.carpediemsolution.fitdiary.utils.MathUtils;

/**
 * Created by Юлия on 22.02.2017.
 */
public class WeightMessageDialog extends DialogFragment {
    private static final String LOG_TAG = "Log";

    Person mPerson;

    private static final String ARG_WEIGHT_TODAY = "weight_today";

    public static WeightMessageDialog newInstance(String weightToday) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEIGHT_TODAY, weightToday);
        WeightMessageDialog dialogFragment = new WeightMessageDialog();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
        String weightToday = (String)getArguments().getSerializable(ARG_WEIGHT_TODAY);
        CalculatorLab sCalcLab = CalculatorLab.get();
        mPerson = sCalcLab.getPerson();


        Person mPerson = sCalcLab.getPerson();
        double changedWeihgt  = Double.parseDouble(weightToday);

       View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.weight_message_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyTheme_Blue_Dialog);

        builder.setTitle(new MathUtils().getHoursOfDate() + ", "+ mPerson.getPersonName() + "!")
                .setMessage(new MathUtils().personIMT(sCalcLab,changedWeihgt) + "\n" +
                        getString(R.string.from_start_weight_changed) + " "
                        + new MathUtils().changingWeight(sCalcLab,changedWeihgt) +  " " + getString(R.string.kg))
        .setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getActivity(), PagerMainActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
