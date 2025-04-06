package com.example.foodapp.fragments.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.foodapp.R;

public class SettingsFragment extends Fragment {

    private RadioGroup themeGroup, notificationGroup;
    private SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        themeGroup = view.findViewById(R.id.theme_group);
        notificationGroup = view.findViewById(R.id.reminder_notification_group);

        // Load saved preferences
        loadPreferences();

        // Set listeners for RadioGroups
        themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.light_theme) {
                savePreference("theme", "light");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                view.invalidate();
            } else if (checkedId == R.id.dark_theme) {
                savePreference("theme", "dark");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                view.invalidate();
            } else if (checkedId == R.id.system_theme) {
                savePreference("theme", "system");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                view.invalidate();
            }
        });

        notificationGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.notification_on) {
                savePreference("notification", true);
            } else {
                savePreference("notification", false);
            }
        });

        ImageView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.popBackStack();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getParentFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }

    private void savePreference(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        }
        editor.apply();
    }

    private void loadPreferences() {
        String savedTheme = sharedPreferences.getString("theme", "light");
        switch (savedTheme) {
            case "light":
                themeGroup.check(R.id.light_theme);
                break;
            case "dark":
                themeGroup.check(R.id.dark_theme);
                break;
            case "system":
                themeGroup.check(R.id.system_theme);
                break;
        }

        // Set notification group
        boolean notification = sharedPreferences.getBoolean("notification", true);
        notificationGroup.check(notification ? R.id.notification_on : R.id.notification_off);
    }
}