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
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.FragmentAccountAdminBinding;
import com.example.foodapp.enums.AccountListFunction;
import com.example.foodapp.fragments.setting.SettingsFragment;
import com.example.foodapp.utils.AuthInterceptor;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.account.AdminAccountViewModel;

public class AdminAccountFragment extends Fragment {
    private FragmentAccountAdminBinding binding;
    private AdminAccountViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), AdminAccountViewModel.class))
                .get(AdminAccountViewModel.class);

        observeUser();
        viewModel.fetchUserProfile();

        binding.revenueStatistics.setOnClickListener(v -> openRevenueStatistics());
        binding.accountListButton.setOnClickListener(v -> openAccountList());
        binding.ordersHistory.setOnClickListener(v -> openOrdersHistory());
        binding.settingsButton.setOnClickListener(v -> openSettings());
    }

    private void observeUser() {
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.accountNameView.setText(user.getFullName());
                loadAvatar(user.getAvatarUrl());
            } else {
                Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAvatar(String url) {
        if (url != null && !url.trim().isEmpty()) {
            String fullUrl = Constants.URL_HOST_SERVER + url;
            Log.d("AvatarURL", fullUrl);

            Glide.with(requireContext())
                    .load(AuthInterceptor.getAuthorizedGlideUrl(fullUrl))
                    .placeholder(R.drawable.avatar_default)
                    .error(R.drawable.avatar_default)
                    .into(binding.accountAvatarView);
        } else {
            binding.accountAvatarView.setImageResource(R.drawable.avatar_default);
        }
    }

    private void openRevenueStatistics() {
        // TODO: Open revenue statistics screen
    }

    private void openAccountList() {
        AccountListFragment fragment = new AccountListFragment();
        fragment.setArguments(createArgs(AccountListFunction.ACCOUNT_LIST));
        openFragment(fragment);
    }

    private void openOrdersHistory() {
        AccountListFragment fragment = new AccountListFragment();
        fragment.setArguments(createArgs(AccountListFunction.ORDER_HISTORY));
        openFragment(fragment);
    }

    private void openSettings() {
        openFragment(new SettingsFragment());
    }

    private Bundle createArgs(AccountListFunction function) {
        Bundle args = new Bundle();
        args.putSerializable("function", function);
        return args;
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
