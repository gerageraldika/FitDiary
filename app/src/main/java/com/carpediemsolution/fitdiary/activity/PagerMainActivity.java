package com.carpediemsolution.fitdiary.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.carpediemsolution.fitdiary.App;
import com.carpediemsolution.fitdiary.R;
import com.carpediemsolution.fitdiary.activity.presenters.PagerMainPresenter;
import com.carpediemsolution.fitdiary.activity.views.PagerMainView;
import com.carpediemsolution.fitdiary.ui.dialogs.StartAppDialog;
import com.carpediemsolution.fitdiary.ui.adapter.LockableViewPager;
import com.carpediemsolution.fitdiary.ui.adapter.MainFragmentPagerAdapter;
import com.carpediemsolution.fitdiary.util.Constants;
import com.carpediemsolution.fitdiary.util.OnBackListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Юлия on 04.03.2017.
 */
public class PagerMainActivity extends MvpAppCompatActivity implements PagerMainView {

    @InjectPresenter
    PagerMainPresenter presenter;
    @BindView(R.id.background_imageview)
    ImageView imageView;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.nest_scrollview)
    NestedScrollView scrollView;
    @BindView(R.id.tab_layout)
    TabLayout tableLayout;
    @BindView(R.id.view_pager)
    LockableViewPager viewPager;

    private static final int REQUEST_PHOTO = 5;
    private static final String START_APP_DIALOG = "StartUpDialog";
    private String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pager_activity);
        ButterKnife.bind(this);

        scrollView.setFillViewport(true);
        setSupportActionBar(toolbar);

        presenter.init(imageView);
        initViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.fragment_menu_list);
        return true;
    }

    private void initViewPager() {
        viewPager.setSwipeable(false);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.titles_tab)));
        tableLayout.setupWithViewPager(viewPager);
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
            Intent graphIntent = new Intent(PagerMainActivity.this, ChartActivity.class);
            startActivity(graphIntent);
            return true;
        }
        if (id == R.id.action_new_weight) {
            if (!App.getFitLab().returnPerson()) {
                FragmentManager fm = getSupportFragmentManager();
                StartAppDialog dialogFragment = new StartAppDialog();
                dialogFragment.show(fm, START_APP_DIALOG);
            } else {
                //переход на новую активити, главная активити не уничтожается, FragmentList не уничтожается
                //onBackPressed из NewWeight: onStart,onResume. Метод onCreate не вызывается, тк фрагмент не был уничтожен
                Intent intent = new Intent(PagerMainActivity.this, NewFitActivity.class);
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
            String imagePath = prefs.getString(Constants.IMAGE, "");
            if (!"".equals(imagePath)) {
                prefs.edit().remove(Constants.IMAGE).apply();
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_imageview));
                imageView.setBackgroundColor(getResources().getColor(R.color.colorDeepBlue));
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions();
                }
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_PHOTO);
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
                String path = cursor.getString(columnIndex);
                cursor.close();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PagerMainActivity.this);
                prefs.edit().putString(Constants.IMAGE, path).apply();

                imageView.setImageBitmap(BitmapFactory
                        .decodeFile(path));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
      /*  if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
            }
        }*/
    }
}















