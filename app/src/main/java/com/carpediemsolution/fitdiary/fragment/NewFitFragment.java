package com.carpediemsolution.fitdiary.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.presenter.NewFitPresenter;
import com.carpediemsolution.fitdiary.ui.dialogs.DatePickerFragment;
import com.carpediemsolution.fitdiary.ui.dialogs.InformationDialog;
import com.carpediemsolution.fitdiary.view.NewFitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Юлия on 18.02.2017.
 */

public class NewFitFragment extends MvpAppCompatFragment implements NewFitView {

    @InjectPresenter
    NewFitPresenter presenter;

    @BindView(R.id.weight_date_new_fragment)
    TextView dateView;
    @BindView(R.id.weight_now)
    AutoCompleteTextView currentWeightView;
    @BindView(R.id.calories_now)
    EditText caloriesEditText;
    @BindView(R.id.notes_now)
    EditText notesEditText;
    @BindView(R.id.calc_photo)
    ImageView mPhotoView;
    @BindView(R.id.fab_weight_write)
    FloatingActionButton fabWriteData;

    private InterstitialAd interstitial;
    private Date date;
    private String photoUri;
    // private static String LOG_TAG = "NewFitFragment";
    private static String LOG_LF = "LifeCycle NewWeight";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0; //константа для кода запроса
    private static final String DIALOG_WEIGHT_MESSAGE = "DialogMessage";
    private static final int REQUEST_TAKE_PHOTO = 1;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    @OnClick(R.id.weight_date_new_fragment)
    public void setWeightDate() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment
                .newInstance(date);
       // FragmentTransaction fragmentTransaction = fm.beginTransaction();
       // fragmentTransaction.addToBackStack(null);
        dialog.setTargetFragment(NewFitFragment.this, REQUEST_DATE);
        dialog.show(fm, DIALOG_DATE);
    }

    @OnClick(R.id.fab_weight_write)
    public void writeData(){
        String weight = currentWeightView.getText().toString();
        String calories = caloriesEditText.getText().toString();
        String notes = notesEditText.getText().toString();

        presenter.saveFit(weight, calories, notes, date, photoUri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(LOG_LF, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_fit, parent, false);
        ButterKnife.bind(this, v);

        date = new Date();
        initPhotoView();
        setStringFilters();
        updateDate();

        fabWriteData.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_white_24dp));

        FloatingActionButton weightPhoto = (FloatingActionButton) v.findViewById(R.id.fab_new_photo);
        weightPhoto.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_a_photo_white_24dp));
        weightPhoto.setOnClickListener((View photoView) -> {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions();
            }
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, REQUEST_TAKE_PHOTO);
        });

        interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId(String.valueOf(R.string.adw));

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
        return v;
    }

    @Override
    public void showSuccess() {
        //to do
    }

    @Override
    public void showError() {
        Toast toast = Toast.makeText(getActivity(), getString(R.string.insert_weight), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showInfo(@NonNull String weight) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InformationDialog dialogFragment = InformationDialog
                .newInstance(weight);
        dialogFragment.show(fm, DIALOG_WEIGHT_MESSAGE);
        //после вызова диалогового фрагмента и смены активити NewWeight не уничтожается, попадает в стэк*/
    }

    @Override
    public void showErrorMessage() {
        Toast toast = Toast.makeText(getActivity(), getString(R.string.insert_correct_weight), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        interstitial.loadAd(adRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE) {
            date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            updateDate();
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK && null != data) {
            try {
                photoUri = presenter.getPhotoUri(data);
                mPhotoView.setImageBitmap(BitmapFactory
                        .decodeFile(photoUri));
                mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
            if (interstitial.isLoaded()) {
                interstitial.show();
            }
        }
    }

    private void setStringFilters(){
        int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        int maxLength = 6;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);

        currentWeightView.setInputType(type);
        currentWeightView.setFilters(FilterArray);
        caloriesEditText.setInputType(type);
        caloriesEditText.setFilters(FilterArray);
    }

    private void initPhotoView(){
        mPhotoView.setAdjustViewBounds(true);
        mPhotoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_perm_identity_white_24dp));
        mPhotoView.setScaleType(ImageView.ScaleType.CENTER);
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(getActivity(), p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            //   return;
        }
    }

    public void updateDate() {
        dateView.setText(DateFormat.format("dd MM yyyy", date));
    }

    //***********************************
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
    public void onResume() {
        super.onResume();
        Log.d(LOG_LF, "onResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_LF, "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        super.onStop();
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

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_LF, "onPause");
    }
}







