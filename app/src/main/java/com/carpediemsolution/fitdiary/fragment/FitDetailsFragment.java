package com.carpediemsolution.fitdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.activity.PhotoViewActivity;
import com.carpediemsolution.fitdiary.activity.PagerMainActivity;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.presenter.FitDetailsPresenter;
import com.carpediemsolution.fitdiary.ui.dialogs.DatePickerFragment;
import com.carpediemsolution.fitdiary.util.OnBackListener;
import com.carpediemsolution.fitdiary.util.async.FitDetailsAsync;
import com.carpediemsolution.fitdiary.view.FitDetailsView;

import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Юлия on 07.02.2017.
 */

public class FitDetailsFragment extends MvpAppCompatFragment implements OnBackListener, FitDetailsView {

    @InjectPresenter
    FitDetailsPresenter presenter;

    @BindView(R.id.view_date)
    Button viewDate;
    @BindView(R.id.view_weight)
    EditText viewWeight;
    @BindView(R.id.view_calories)
    EditText viewCalories;
    @BindView(R.id.view_notes)
    EditText viewNotes;
    @BindView(R.id.view_photo)
    ImageView viewPhoto;

    private Date date;
    private UUID weightId;
    // private static String LOG_TAG = "FitDetailsFragment";
    private static String LOG_LF = "LifeCycle WeightInfo";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0; //константа для кода запроса
    private static final String ARG_WEIGHT_ID = "weight_id";
    private boolean isEnabledPhoto = true;


    public static FitDetailsFragment newInstance(UUID weightId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEIGHT_ID, weightId);
        FitDetailsFragment fragment = new FitDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        weightId = (UUID) getArguments().getSerializable(ARG_WEIGHT_ID);
        presenter.getWeight(weightId);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calculator_fragment, parent, false);
        ButterKnife.bind(this, v);

        FloatingActionButton buttonForRewrite = (FloatingActionButton) v.findViewById(R.id.button_for_rewrite);
        buttonForRewrite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_create_white_24dp));
        buttonForRewrite.setOnClickListener(new View.OnClickListener() {
            boolean isEnabledText = true;

            @Override
            public void onClick(View view) {
                if (!isEnabledText) { //enabled for update
                    viewWeight.setEnabled(false);
                    viewNotes.setEnabled(false);
                    viewDate.setEnabled(false);
                    viewCalories.setEnabled(false);
                    buttonForRewrite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_create_white_24dp));
                    presenter.updateWeight(weightId, viewWeight.getText().toString(),
                            viewCalories.getText().toString(), viewNotes.getText().toString(), date);
                    isEnabledText = true;
                } else {
                    viewWeight.setEnabled(true);
                    viewNotes.setEnabled(true);
                    viewDate.setEnabled(true);
                    viewCalories.setEnabled(true);
                    isEnabledText = false;
                    buttonForRewrite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_white_24dp));
                }
            }
        });
        return v;
    }

    @Override
    public void showSuccess() {
        //to do
    }

    @Override
    public void showError() {
        Toast toast = Toast.makeText(getActivity(), getString(R.string.insert_weight)
                , Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showErrorMessage() {
        Toast toast = Toast.makeText(getActivity(),
                getString(R.string.insert_correct_weight), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showFitDetails(Weight weight) {
        viewDate.setText(DateFormat.format("dd MM yyyy", weight.getDate()));
        viewDate.setEnabled(false);
        viewDate.setOnClickListener((View viewDate) -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            DatePickerFragment dialog = DatePickerFragment
                    .newInstance(weight.getDate());
            dialog.setTargetFragment(FitDetailsFragment.this, REQUEST_DATE);
            dialog.show(fm, DIALOG_DATE);//зачем константа?
        });

        int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        int maxLength = 6;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);

        viewWeight.setInputType(type);
        viewWeight.setFilters(FilterArray);
        viewWeight.setText(weight.getsWeight());
        viewWeight.setEnabled(false);


        viewCalories.setInputType(type);
        viewCalories.setFilters(FilterArray);
        viewCalories.setText(weight.getCalories());
        viewCalories.setEnabled(false);

        viewNotes.setText(weight.getNotes());
        viewNotes.setEnabled(false);

        viewPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_perm_identity_white_24dp));
        viewPhoto.setScaleType(ImageView.ScaleType.CENTER);

        showAsynkPhoto(weight); //


        viewPhoto.setOnClickListener((View photoView) -> {
            if (!isEnabledPhoto) {
                Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                intent.putExtra("weight_uri", weight.getPhotoUri());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getActivity(), PagerMainActivity.class);
        startActivity(i);
    }

    public void showAsynkPhoto(Weight weight) {
        if (weight.getPhotoUri() != null) {
            try {
                new FitDetailsAsync(viewPhoto, weight, isEnabledPhoto).execute();
            } catch (Exception ex) {
                viewPhoto.setImageResource(R.drawable.ic_perm_identity_white_24dp);
            }
        } else viewPhoto.setImageResource(R.drawable.ic_perm_identity_white_24dp);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE) {
            date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // mWeight.setDate(date);

            updateDate();
        }
    }

    public void updateDate() {
        viewDate.setText(DateFormat.format("dd MM yyyy", date));
    }

    //************************************************************************************
    // just for studying =)
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









