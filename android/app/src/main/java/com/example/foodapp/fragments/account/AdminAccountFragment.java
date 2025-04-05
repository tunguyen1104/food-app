package com.example.foodapp.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentAccountAdminBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.fragments.setting.SettingsFragment;
import com.example.foodapp.repositories.UserRepository;

public class AdminAccountFragment extends Fragment {
    private FragmentAccountAdminBinding binding;
    private TextView accountNameView;
    private ImageView accountAvatarView;
    private UserRepository userRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountAdminBinding.inflate(inflater, container, false);

        // Initialize views
        accountAvatarView = binding.accountAvatarView;
        accountNameView = binding.accountNameView;

        // Load saved data
        userRepository = new UserRepository(requireContext());
        loadAccountInfo();

        // Buttons
        binding.revenueStatistics.setOnClickListener(v -> openRevenueStatistics());
        binding.accountListButton.setOnClickListener(v -> openAccountList());
        binding.ordersHistory.setOnClickListener(v -> openOrdersHistory());
        binding.settingsButton.setOnClickListener(v -> openSettings());

        return binding.getRoot();
    }

    private void loadAccountInfo() {
        userRepository.getUserProfile(new UserRepository.UserProfileCallback() {
            @Override
            public void onSuccess(UserResponse user) {
                accountNameView.setText(user.getFullName());
                loadAvatar(user.getAvatarUrl());
            }

            @Override
            public void onError(String message) {
                if (isAdded()) Toast.makeText(requireContext(), "Failed to load information: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAvatar(String url) {
        if (!isAdded()) return;

        if (url != null && !url.isEmpty()) {
            Glide.with(requireContext())
                    .load(url)
                    .placeholder(R.drawable.avatar_default)
                    .into(accountAvatarView);
        } else {
            accountAvatarView.setImageResource(R.drawable.avatar_default);
        }
    }

    private void openRevenueStatistics() {
    }

    private void openAccountList() {
    }

    private void openOrdersHistory() {
    }

    private void openSettings() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.accountContainer, new SettingsFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}