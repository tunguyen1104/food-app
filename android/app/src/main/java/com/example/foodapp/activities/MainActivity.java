package com.example.foodapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.R;
import com.example.foodapp.adapters.ScreenSlidePagerAdapter;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private BottomNavigationView navView;
    private static final int REQUEST_CODE_POST_NOTIFICATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setupViewPager();
        loadTheme();
        initializeViewModels();
        checkAndRequestNotificationPermission();
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

    private void checkAndRequestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_CODE_POST_NOTIFICATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATION) {
            String message = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ? "Notification permission granted" : "Notification permission denied";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
