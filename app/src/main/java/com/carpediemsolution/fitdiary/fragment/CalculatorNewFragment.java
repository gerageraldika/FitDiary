package com.carpediemsolution.fitdiary.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.ui_utils.WeightMessageDialog;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.dao.CalculatorLab;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Юлия on 18.02.2017.
 */

public class CalculatorNewFragment extends Fragment {

    @BindView(R.id.weight_date)
    TextView weightDateView;
    @BindView(R.id.weight_now)
    AutoCompleteTextView currentWeightView;
    @BindView(R.id.calories_now)
    EditText caloriesEditText;


    @OnClick(R.id.weight_date)
    public void setWeightDate(CharSequence s) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment
                .newInstance(mWeight.getDate());
        dialog.setTargetFragment(CalculatorNewFragment.this, REQUEST_DATE);
        dialog.show(fm, DIALOG_DATE);
    }

    @OnTextChanged(R.id.weight_now)
    public void setUserWeight(CharSequence s) {
        mWeight.setWeight(s.toString());
    }

    @OnTextChanged(R.id.notes_now)
    public void setUserNotes(CharSequence s) {
        mWeight.setNotes(s.toString());
    }

    @OnTextChanged(R.id.notes_now)
    public void setUserCalories(CharSequence s) {
        mWeight.setCalories(s.toString());
    }

    private ImageView mPhotoView;
    private InterstitialAd interstitial;
    private Weight mWeight;
    private CalculatorLab sCalcLab;
    private static String LOG_TAG = "CalculatorNewFragment";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0; //константа для кода запроса
    private static final String DIALOG_WEIGHT_MESSAGE = "DialogMessage";
    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = UUID.randomUUID();
        mWeight = new Weight(id);
    }

    @Override
    public void onPause() {
        super.onPause();
        sCalcLab.updateWeight(mWeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_calculator, parent, false);
        ButterKnife.bind(this, getActivity());

        sCalcLab = CalculatorLab.get();

        int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        int maxLength = 6;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);

        currentWeightView.setInputType(type);
        currentWeightView.setFilters(FilterArray);

        caloriesEditText.setInputType(type);
        caloriesEditText.setFilters(FilterArray);

        updateDate();

        FloatingActionButton fabWriteData = (FloatingActionButton) v.findViewById(R.id.fab_weight_write);
        fabWriteData.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_white_24dp));
        fabWriteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWeight.getsWeight() == null) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.insert_weight), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Double.parseDouble(String.valueOf(mWeight.getsWeight())) <= 300) {
                    sCalcLab.addWeight(mWeight);
                    Log.d(LOG_TAG, "---addweight" + mWeight.getPhotoUri());
                    sCalcLab.updateWeight(mWeight);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    WeightMessageDialog dialogFragment = WeightMessageDialog
                            .newInstance(mWeight.getsWeight());
                    dialogFragment.show(fm, DIALOG_WEIGHT_MESSAGE);

                } else if (Double.parseDouble(String.valueOf(mWeight.getsWeight())) > 300) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.insert_correct_weight), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.calc_photo);
        mPhotoView.setAdjustViewBounds(true);

        mPhotoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_perm_identity_white_24dp));
        mPhotoView.setScaleType(ImageView.ScaleType.CENTER);

        FloatingActionButton weightPhoto = (FloatingActionButton) v.findViewById(R.id.fab_new_photo);
        weightPhoto.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_a_photo_white_24dp));
        weightPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions();
                }
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_TAKE_PHOTO);
            }
        });

        interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId("ca-app-pub-9016583513972837/7607885104");

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
        return v;
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
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mWeight.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                mPhotoView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mWeight.setPhotoUri(imgDecodableString);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
            if (interstitial.isLoaded()) {
                interstitial.show();
            }
        }
    }

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};

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
            return;
        }
    }

    public void updateDate() {
        weightDateView.setText(DateFormat.format("dd MM yyyy", mWeight.getDate()));
    }
}







