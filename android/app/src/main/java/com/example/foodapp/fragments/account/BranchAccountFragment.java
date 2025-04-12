package com.example.foodapp.fragments.account;

import static com.example.foodapp.consts.Constants.ARG_USER;

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
import com.example.foodapp.databinding.FragmentAccountBranchBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.enums.AccountListFunction;
import com.example.foodapp.fragments.setting.SettingsFragment;
import com.example.foodapp.utils.AuthInterceptor;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.account.BranchAccountViewModel;

public class BranchAccountFragment extends Fragment {
    private FragmentAccountBranchBinding binding;
    private BranchAccountViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBranchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), BranchAccountViewModel.class))
                .get(BranchAccountViewModel.class);

        observeUser();
        viewModel.fetchUserProfile();

        binding.btnBranchInfo.setOnClickListener(v -> openAccountInfo());
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

    private void openAccountInfo() {
        BranchInfoFragment fragment = new BranchInfoFragment();
        fragment.setArguments(createUserArgs(viewModel.getUser().getValue()));
        openFragment(fragment);
    }

    private void openOrdersHistory() {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        fragment.setArguments(createUserArgs(viewModel.getUser().getValue()));
        openFragment(fragment);
    }

    private Bundle createUserArgs(@NonNull final UserResponse user) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_USER, user);
        return bundle;
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
