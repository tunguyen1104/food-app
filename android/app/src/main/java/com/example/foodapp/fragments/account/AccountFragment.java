package com.example.foodapp.fragments.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentAccountBranchBinding;
import com.example.foodapp.fragments.setting.SettingsFragment;
import com.example.foodapp.utils.GlideUtils;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.account.AccountViewModel;

public class AccountFragment extends Fragment {
    private FragmentAccountBranchBinding binding;
    private AccountViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBranchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), AccountViewModel.class))
                .get(AccountViewModel.class);

        observeUserProfile();

        viewModel.fetchUserProfile();

        binding.btnBranchInfo.setOnClickListener(v -> openAccountInfo());
        binding.ordersHistory.setOnClickListener(v -> openOrderHistory());
        binding.settingsButton.setOnClickListener(v -> openSettings());
    }

    private void observeUserProfile() {
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.accountNameView.setText(user.getFullName());
                loadAvatar(user.getAvatarUrl());
            } else {
                Toast.makeText(requireContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAvatar(String url) {
        if (url != null && !url.trim().isEmpty()) {
            String fullUrl = com.example.foodapp.consts.Constants.URL_HOST_SERVER + url;
            Log.d("AvatarURL", fullUrl);

            Glide.with(requireContext())
                    .load(GlideUtils.getAuthorizedGlideUrl(getContext(), fullUrl))
                    .placeholder(R.drawable.avatar_default)
                    .error(R.drawable.avatar_default)
                    .into(binding.accountAvatarView);
        } else {
            binding.accountAvatarView.setImageResource(R.drawable.avatar_default);
        }
    }

    private void openAccountInfo() {
        BranchInfoFragment fragment = new BranchInfoFragment();
        if (viewModel.getUserProfile().getValue() != null) {
            Bundle args = new Bundle();
            args.putSerializable("user", viewModel.getUserProfile().getValue());
            fragment.setArguments(args);
        }
        openFragment(fragment);
    }

    private void openOrderHistory() {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        if (viewModel.getUserProfile().getValue() != null) {
            Bundle args = new Bundle();
            args.putSerializable("user", viewModel.getUserProfile().getValue());
            fragment.setArguments(args);
        }
        openFragment(fragment);
    }

    private void openSettings() {
        openFragment(new SettingsFragment());
    }

    private void openFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.accountContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
