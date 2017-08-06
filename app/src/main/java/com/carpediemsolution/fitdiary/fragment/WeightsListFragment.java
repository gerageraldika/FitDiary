package com.carpediemsolution.fitdiary.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carpediemsolution.fitdiary.activity.CalculatorPagerActivity;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.database.DbSchema.CalculatorTable;
import com.carpediemsolution.fitdiary.model.Person;
import com.carpediemsolution.fitdiary.model.Weight;
import com.carpediemsolution.fitdiary.utils.OnBackListener;
import com.carpediemsolution.fitdiary.utils.PictureUtils;

import java.io.File;
import java.util.List;


/**
 * Created by Юлия on 11.02.2017.
 */

public class WeightsListFragment extends Fragment implements OnBackListener {

    private RecyclerView mCrimeRecyclerView;
    private CalcAdapter mAdapter;
    private List<Weight> weights;
    private Weight weight;
    private static final String LOG_TAG = "WeightsListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_list, container, false);

        FitLab sCalcLab = FitLab.get();
        weights = sCalcLab.getWeights();
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.weight_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mCrimeRecyclerView); //set swipe to recylcerview

        return view;}

    private void updateUI() {
        mAdapter = new CalcAdapter();
        mCrimeRecyclerView.setAdapter(mAdapter);
        mAdapter.setWeights(weights);
        mAdapter.notifyDataSetChanged();}

    @Override
    public void onResume() {
        super.onResume();
        updateUI();}

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;}

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition(); //get position which is swipe

            if (direction == ItemTouchHelper.LEFT) {    //if swipe left

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyTheme_Blue_Dialog); //alert for confirm to delete
                builder.setMessage(getString(R.string.sure_to_delete));    //set message
                builder.setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() { //when click on DELETE
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.notifyItemRemoved(position);//item removed from recylcerview
                        weight = weights.get(position);
                        String uuidString = weight.getId().toString();
                        FitLab.get().mDatabase.delete(CalculatorTable.NAME,
                                CalculatorTable.Cols.UUID + " = '" + uuidString + "'", null);
                        weights.remove(position);  //then remove item
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                    }
                }).show();}
        }
    };

    @Override
    public void onBackPressed() {
        getActivity().finishAffinity();
    }

    private class CalcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int EMPTY_VIEW = 10;
        private Weight mWeight;

        private FitLab sCalcLab = FitLab.get();
        private List<Weight> mWeights;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mCaloriesTextView;
        private ImageView mPhotoView;
        private TextView mIMTView;
        Person mPerson = sCalcLab.getPerson();

        private class EmptyViewHolder extends RecyclerView.ViewHolder {
            private EmptyViewHolder(View itemView) {
                super(itemView);}
        }

        private class CalcHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private CalcHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_weight_title_text_view);
                mDateTextView = (TextView) itemView.findViewById(R.id.list_item__date_text_view);
                mPhotoView = (ImageView) itemView.findViewById(R.id.list_item_photo);
                mIMTView = (TextView) itemView.findViewById(R.id.list_item__imt_text_view);
                mCaloriesTextView = (TextView) itemView.findViewById(R.id.list_item__calories_text_view);}

            public void onClick(View v) {
                int position = getLayoutPosition();
                mWeight = mWeights.get(position);
                Intent intent = CalculatorPagerActivity.newIntent(getActivity(), mWeight.getId());
                startActivity(intent);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mWeights.size() == 0) {
                return EMPTY_VIEW;
            } else {
                return super.getItemViewType(position);}
        }

        @Override
        public int getItemCount() {
            return mWeights.size() > 0 ? mWeights.size() : 1;
        }

        public void setWeights(List<Weight> weights) {
            mWeights = weights;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;

            if (viewType == EMPTY_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
                EmptyViewHolder evh = new EmptyViewHolder(v);
                return evh;
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
                CalcHolder vh = new CalcHolder(v);
                return vh;}
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder vho, int position) {
            if (vho instanceof CalcHolder) {
                CalcHolder vh = (CalcHolder) vho;
                mWeight = mWeights.get(position);
                mDateTextView.setText(DateFormat.format("dd MM yyyy", mWeight.getDate()));
                mTitleTextView.setText((getString(R.string.weight)) + " " + mWeight.getsWeight() + " " + (getString(R.string.kg)));

                if (mWeight.getCalories() != null) {
                    mCaloriesTextView.setText( (getString(R.string.calorii)) + " " + mWeight.getCalories());}
                mPhotoView.setImageResource(R.drawable.ic_perm_identity_white_24dp);

                double personIMT = Math.round(Double.parseDouble(mWeight.getsWeight()) / ((Math.pow((Double.parseDouble(mPerson.getPersonHeight())), 2)) / 10000));
                String personIMTtoday = String.valueOf(personIMT);
                mIMTView.setText("  /  " + (getString(R.string.imt)) + " " + personIMTtoday);

                new PhotoItemsTask(mPhotoView, mWeight).execute();}
        }


        private class PhotoItemsTask extends AsyncTask<Void, Void, Bitmap> {
            ImageView mPhotoView;
            Weight mWeight;

            private PhotoItemsTask(ImageView mPhotoView, Weight mWeight) {
                this.mPhotoView = mPhotoView;
                this.mWeight = mWeight;}

            @Override
            protected Bitmap doInBackground(Void... param) {
                Bitmap bitmap = null;
                try {
                    Uri imageUri = Uri.parse(mWeight.getPhotoUri());
                    File photoFile = new File(imageUri.getPath());

                    bitmap = PictureUtils.getScaledBitmap(
                            photoFile.getPath(), getActivity());

                } catch (NullPointerException e) {
                    bitmap = null;}
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    bitmap = PictureUtils.cropToSquare(bitmap);
                    RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.
                         create(getResources(), bitmap);
                    roundDrawable.setCircular(true);

                    mPhotoView.setImageDrawable(roundDrawable);
                    mPhotoView.setScaleType(ImageView.ScaleType.FIT_XY);
                } else if (bitmap == null) {
                    mPhotoView.setImageResource(R.drawable.ic_perm_identity_white_24dp);
                }
            }
        }
    }
}


















