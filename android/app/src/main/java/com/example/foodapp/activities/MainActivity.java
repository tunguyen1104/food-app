package com.example.foodapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.R;
import com.example.foodapp.adapters.ScreenSlidePagerAdapter;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.network.StompManager;
import com.example.foodapp.utils.NotificationPermissionHelper;
import com.example.foodapp.utils.UserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private BottomNavigationView navView;
    private Disposable notifDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeViews();
        setupViewPager();
        loadTheme();
        initializeViewModels();
        NotificationPermissionHelper.requestIfNeeded(this);
        subscribeNotification();
    }

    private void initializeViews() {
        navView = binding.navView;
        viewPager = binding.viewPager;
    }

    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (id == R.id.navigation_orders) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (id == R.id.navigation_message) {
                viewPager.setCurrentItem(2);
                return true;
            } else if (id == R.id.navigation_account) {
                viewPager.setCurrentItem(3);
                return true;
            }

            return false;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateNavigationBarSelection(position);
            }
        });
    }

    private void updateNavigationBarSelection(int position) {
        switch (position) {
            case 0:
                navView.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                navView.setSelectedItemId(R.id.navigation_orders);
                break;
            case 2:
                navView.setSelectedItemId(R.id.navigation_message);
                break;
            case 3:
                navView.setSelectedItemId(R.id.navigation_account);
                break;
        }
    }

    private void loadTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String savedTheme = sharedPreferences.getString("theme", "light");
        AppCompatDelegate.setDefaultNightMode(savedTheme.equals("light") ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void initializeViewModels() {
        // ViewModel initialization logic goes here
    }

    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] p, @NonNull int[] r) {
        super.onRequestPermissionsResult(code, p, r);
        if (code == NotificationPermissionHelper.REQ_POST_NOTI) {
            boolean granted = r.length > 0 && r[0] == PackageManager.PERMISSION_GRANTED;
            Toast.makeText(this,
                    granted ? R.string.perm_noti_granted : R.string.perm_noti_denied,
                    Toast.LENGTH_SHORT).show();

            if (granted && (notifDisp == null || notifDisp.isDisposed())) {
                subscribeNotification();
            }
        }
    }

    private void subscribeNotification() {
        UserResponse user = UserManager.getUser(this);
        if (user == null) return;

        StompManager.getInstance().connect();

        notifDisp = StompManager.getInstance()
                .subscribeToNotifications(this, String.valueOf(user.getId()));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        super.finish();
        notifDisp.dispose();
    }

    @Override
    protected void onDestroy() {
        if (notifDisp != null && !notifDisp.isDisposed()) notifDisp.dispose();
        super.onDestroy();
    }

    public interface PermissionCallback {
        void onPermissionResult(boolean isGranted);
    }
}
