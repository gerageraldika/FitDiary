package com.carpediemsolution.fitdiary.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.ui_utils.StartAppDialog;
import com.carpediemsolution.fitdiary.database.DbSchema;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.adapter.LockableViewPager;
import com.carpediemsolution.fitdiary.adapter.MainFragmentPagerAdapter;
import com.carpediemsolution.fitdiary.utils.OnBackListener;
import com.carpediemsolution.fitdiary.utils.PictureUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Юлия on 04.03.2017.
 */
public class PagerMainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Toolbar mToolbar;
    private static final int REQUEST_PHOTO = 5;
    private static final String START_APP_DIALOG = "StartUpDialog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pager_activity);

        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        imageView = (ImageView) findViewById(R.id.background_imageview);
        new PhotoLoadingTask(imageView).execute();

        TabLayout mTableLayout = (TabLayout) findViewById(R.id.tab_layout);
        LockableViewPager mViewPager = (LockableViewPager) findViewById(R.id.view_pager);
        mViewPager.setSwipeable(false);

        mViewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.titles_tab)));

        mTableLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.fragment_menu_list);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_remind) {
            Intent remindIntent = new Intent(PagerMainActivity.this, ReminderActivity.class);
            startActivity(remindIntent);
            return true;
        }

        if (id == R.id.action_show_graph) {
            Intent graphIntent = new Intent(PagerMainActivity.this, GraphicActivity.class);
            startActivity(graphIntent);

            return true;
        }

        if (id == R.id.action_new_weight) {
            if (DbSchema.CalculatorTable.NAME_PERSON == null ||
                    FitLab.get().returnPerson() == false ||
                    DbSchema.CalculatorTable.NAME_PERSON.isEmpty()) {
                FragmentManager fm = getSupportFragmentManager();
                StartAppDialog dialogFragment = new StartAppDialog();
                dialogFragment.show(fm, START_APP_DIALOG);
            } else {
                Intent intent = new Intent(PagerMainActivity.this, CalculatorNewActivity.class);
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.menu_item_person_data) {
            FragmentManager fm = getSupportFragmentManager();
            StartAppDialog dialogFragment = new StartAppDialog();
            dialogFragment.show(fm, START_APP_DIALOG);
            return true;
        }
        if (id == R.id.menu_item_change_image) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PagerMainActivity.this);
            String imagePath = prefs.getString("Image", "");
            Bitmap bitmap = null;
            if (imagePath != null) {
                if (!imagePath.equals("")) {
                    prefs.edit().remove("Image").apply();
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_imageview));
                    imageView.setBackgroundColor(getResources().getColor(R.color.colorDeepBlue));
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        checkPermissions();
                    }
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_PHOTO);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PHOTO && resultCode == PagerMainActivity.this.RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = PagerMainActivity.this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);


                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PagerMainActivity.this);
                prefs.edit().putString("Image", imgDecodableString).apply();

                imageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
            } catch (Exception e) {
                Toast.makeText(PagerMainActivity.this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackListener) {
                backPressedListener = (OnBackListener) fragment;
                break;
            }
        }
        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(PagerMainActivity.this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(PagerMainActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
            }
        }
    }


    private class PhotoLoadingTask extends AsyncTask<Void, Void, Bitmap> {
        ImageView mPhotoView;

        private PhotoLoadingTask(ImageView mPhotoView) {
            this.mPhotoView = mPhotoView;
        }

        @Override
        protected Bitmap doInBackground(Void... param) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PagerMainActivity.this);
            String imagePath = prefs.getString("Image", "");
            Bitmap bitmap = null;
            if (imagePath != null) {
                if (!imagePath.equals("")) {
                    bitmap = PictureUtils.getScaledBitmap(
                            imagePath, PagerMainActivity.this);

                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
             //
            } else {
                mPhotoView.setImageBitmap(bitmap);
            }
        }
    }
}















