package com.carpediemsolution.fitdiary.ui.dialogs;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.util.MathUtils;

/**
 * Created by Юлия on 22.02.2017.
 */

public class StartAppDialog extends DialogFragment {
    private EditText personName;
    private EditText getPersonHeight;
    private EditText getPersonWeight;
    private Person person=Person.get();
    private static final String MY_LOG  = "StartDialogLog";
    private FitLab sCalcLab;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
         .inflate(R.layout.start_app_dialog, null);

        sCalcLab = FitLab.get();

        Log.d(MY_LOG, "----Person" + v);

        int type = InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER ;
        int maxLength = 6;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);


        personName = (EditText)v.findViewById(R.id.person_name);
        getPersonHeight = (EditText)v.findViewById(R.id.person_height);
        getPersonWeight = (EditText)v.findViewById(R.id.person_weight);


        personName.setText(person.getPersonName());
        personName.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         }
         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             person.setPersonName(s.toString());
             Log.d(MY_LOG, "----Person" + person.getPersonName());
         }
         @Override
         public void afterTextChanged(Editable s) {

         }
         });

        getPersonHeight.setText(person.getPersonHeight());
        getPersonHeight.setRawInputType(type);
        getPersonHeight.setFilters(FilterArray);
        getPersonHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                person.setPersonHeight(s.toString());
                Log.d(MY_LOG, "----Person" + person.getPersonHeight());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getPersonWeight.setText(person.getPersonWeight());
        getPersonWeight.setRawInputType(type);
        getPersonWeight.setFilters(FilterArray);
        getPersonWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                person.setPersonWeight(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyTheme_Blue_Dialog);

        if(sCalcLab.getPerson() == null)
           {
            builder.setTitle(new MathUtils().getHoursOfDate()+"!");
            builder.setMessage("\n"+
                    getString(R.string.start_app_message)+ "\n")
            .setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if((person.getPersonName() != null) && (person.getPersonHeight() !=null)
                                    &&(person.getPersonWeight() != null) &&
                                    (Double.parseDouble(String.valueOf(person.getPersonHeight())) > 0)&&
                                    (Double.parseDouble(String.valueOf(person.getPersonHeight())) <= 250)&&
                                    (Double.parseDouble(String.valueOf(person.getPersonWeight())) > 0)&&
                                    (Double.parseDouble(String.valueOf(person.getPersonWeight())) <= 300 )) {
                                sCalcLab.addPerson(person);

                                Log.d(MY_LOG, "----addPerson" + person.getPersonName() +
                                        "---" + person.getPersonHeight());
                            }

                                else if (person.getPersonName() == null ||person.getPersonHeight() == null
                                    || person.getPersonWeight() == null) {
                                    Toast toast = Toast.makeText(getActivity(),
                                            getString(R.string.correct_data), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                } else  { Toast toast = Toast.makeText(getActivity(),
                                    getString(R.string.correct_personal_data), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                }
                            dialog.cancel();
                        }
                    });
        }
         else if((sCalcLab.getPerson() != null))      {
            builder.setTitle(new MathUtils().getHoursOfDate()+ ", " + person.getPersonName() +"!")
                    .setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if((person.getPersonName() != null) && (person.getPersonHeight() !=null)
                                    &&(person.getPersonWeight() != null) &&
                                    (Double.parseDouble(String.valueOf(person.getPersonHeight())) > 0)&&
                                    (Double.parseDouble(String.valueOf(person.getPersonHeight())) <= 250)&&
                                    (Double.parseDouble(String.valueOf(person.getPersonWeight())) > 0)&&
                                    (Double.parseDouble(String.valueOf(person.getPersonWeight())) <= 300 )) {
                                sCalcLab.updatePerson(person);
                                Log.d(MY_LOG, "----upDatePerson" + person.getPersonName() +
                                        "---" +person.getPersonHeight());

                                Log.d(MY_LOG, "----addPerson" + person.getPersonName() +
                                        "---" + person.getPersonHeight());
                            }

                            else if (person.getPersonName() == null ||person.getPersonHeight() == null
                                    || person.getPersonWeight() == null) {
                                Toast toast = Toast.makeText(getActivity(),
                                        getString(R.string.correct_data), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else  { Toast toast = Toast.makeText(getActivity(),
                                    getString(R.string.correct_personal_data), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            dialog.cancel();
                        }
                    });
        }
        builder.setView(v);
        return builder.create();
    }
}
