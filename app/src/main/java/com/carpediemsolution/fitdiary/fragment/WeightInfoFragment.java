package com.carpediemsolution.fitdiary.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.carpediemsolution.fitdiary.activity.OpenPhotoForViewActivity;
import com.carpediemsolution.fitdiary.activity.PagerMainActivity;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.ui_utils.DatePickerFragment;
import com.carpediemsolution.fitdiary.utils.OnBackListener;
import com.carpediemsolution.fitdiary.utils.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;


/**
 * Created by Юлия on 07.02.2017.
 */

public class WeightInfoFragment extends Fragment implements OnBackListener {

    private Button viewDate;
    private EditText viewWeight;
    private EditText viewCalories;
    private EditText viewNotes;
    private ImageView viewPhoto;
    private Weight mWeight;
    private UUID weightId;
    private FitLab sCalcLab;

    private static String LOG_TAG = "WeightInfoFragment";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0; //константа для кода запроса
    private static final String ARG_WEIGHT_ID = "weight_id";
    private boolean isEnabledPhoto = true;


    public static WeightInfoFragment newInstance(UUID weightId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEIGHT_ID, weightId);
        WeightInfoFragment fragment = new WeightInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        weightId = (UUID) getArguments().getSerializable(ARG_WEIGHT_ID);
        sCalcLab = FitLab.get();
        mWeight = sCalcLab.getWeight(weightId);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calculator_fragment, parent, false);

        weightId = (UUID) getArguments().getSerializable(ARG_WEIGHT_ID);
        mWeight = sCalcLab.getWeight(weightId);

        int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        int maxLength = 6;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);


        viewDate = (Button) v.findViewById(R.id.view_date);
        viewDate.setText(DateFormat.format("dd MM yyyy", mWeight.getDate()));
        viewDate.setEnabled(false);
        viewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mWeight.getDate());
                dialog.setTargetFragment(WeightInfoFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);//зачем константа?
            }
        });

        viewWeight = (EditText) v.findViewById(R.id.view_weight);
        viewWeight.setInputType(type);
        viewWeight.setFilters(FilterArray);
        viewWeight.setText(mWeight.getsWeight());
        viewWeight.setEnabled(false);

        viewCalories = (EditText) v.findViewById(R.id.view_calories);
        viewCalories.setInputType(type);
        viewCalories.setFilters(FilterArray);
        viewCalories.setText(mWeight.getCalories());
        viewCalories.setEnabled(false);

        viewNotes = (EditText) v.findViewById(R.id.view_notes);
        viewNotes.setText(mWeight.getNotes());
        viewNotes.setEnabled(false);


        viewPhoto = (ImageView) v.findViewById(R.id.view_photo);
        viewPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_perm_identity_white_24dp));
        viewPhoto.setScaleType(ImageView.ScaleType.CENTER);
        // showPhotoInHolder(mWeight);
        showAsynkPhoto(mWeight);


        viewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "----" + "mWeight intent" + mWeight.getPhotoUri());

                if (!isEnabledPhoto) {
                    Intent intent = new Intent(getActivity(), OpenPhotoForViewActivity.class);
                    intent.putExtra("weight_uri", mWeight.getPhotoUri());
                    startActivity(intent);
                }
            }

        });


        viewWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWeight.setWeight(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWeight.setNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        viewCalories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWeight.setCalories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final FloatingActionButton buttonForRewrite = (FloatingActionButton) v.findViewById(R.id.button_for_rewrite);
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
                    if (mWeight.getsWeight() == null) {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.insert_weight)
                                , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else if (Double.parseDouble(String.valueOf(mWeight.getsWeight())) <= 300) {
                        sCalcLab.updateWeight(mWeight);

                    } else if (Double.parseDouble(String.valueOf(mWeight.getsWeight())) > 300) {
                        Toast toast = Toast.makeText(getActivity(),
                                getString(R.string.insert_correct_weight), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    Log.d(LOG_TAG, "-----" + mWeight.getsWeight());
                    isEnabledText = true;

                } else {
                    viewWeight.setEnabled(true);
                    viewNotes.setEnabled(true);
                    viewDate.setEnabled(true);
                    viewCalories.setEnabled(true);
                    Log.d(LOG_TAG, "-----" + mWeight.getsWeight());
                    isEnabledText = false;
                    buttonForRewrite.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_white_24dp));
                }
            }
        });
        return v;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getActivity(), PagerMainActivity.class);
        startActivity(i);
    }

    public void showAsynkPhoto(Weight weight) {
        if (mWeight.getPhotoUri() != null) {
            try {
                new PhotoHolderTask(viewPhoto, mWeight).execute();
            } catch (Exception ex) {
                viewPhoto.setImageResource(R.drawable.ic_perm_identity_white_24dp);
            }
        } else viewPhoto.setImageResource(R.drawable.ic_perm_identity_white_24dp);
    }

    public Bitmap getPhotoFile() throws NullPointerException, IOException {
        Uri imageUri = Uri.parse(mWeight.getPhotoUri());
        File photoFile = new File(imageUri.getPath());

        Bitmap bitmap;
        if (photoFile.exists()) {
            bitmap = PictureUtils.getScaledBitmap(
                    photoFile.getPath(), getActivity());
            Log.d(LOG_TAG, "----" + "getPhotoFile" + mWeight.getPhotoUri() + "---" + mWeight.getId());
        } else bitmap = null;
        return bitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mWeight.setDate(date);
            updateDate();
        }
    }

    public void updateDate() {
        viewDate.setText(DateFormat.format("dd MM yyyy", mWeight.getDate()));
    }


    private class PhotoHolderTask extends AsyncTask<Void, Void, Bitmap> {
        ImageView mPhotoView;
        Weight mWeight;

        private PhotoHolderTask(ImageView mPhotoView, Weight mWeight) {
            this.mPhotoView = mPhotoView;
            this.mWeight = mWeight;
        }

        @Override
        protected Bitmap doInBackground(Void... param) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                viewPhoto.setImageResource(R.drawable.ic_perm_identity_white_24dp);
            } else {
                viewPhoto.setImageBitmap(bitmap);
                viewPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                isEnabledPhoto = false;
            }
        }
    }
}









