package com.example.foodapp.utils;

import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class NavigationUtil {
    public static void setupBackNavigation(Fragment fragment, View backButton) {
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                FragmentManager fm = fragment.getParentFragmentManager();
                fm.popBackStack();
            });
        }

        fragment.requireActivity().getOnBackPressedDispatcher().addCallback(fragment.getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = fragment.getParentFragmentManager();
                fm.popBackStack();
            }
        });
    }
}
