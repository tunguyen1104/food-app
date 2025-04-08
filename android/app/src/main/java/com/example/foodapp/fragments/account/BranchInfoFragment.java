package com.example.foodapp.fragments.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.consts.Constants;
import com.example.foodapp.databinding.FragmentBranchInfoBinding;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.utils.AuthInterceptor;
import com.example.foodapp.utils.NavigationUtil;

public class BranchInfoFragment extends Fragment {
    private FragmentBranchInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBranchInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationUtil.setupBackNavigation(this, binding.buttonBack);

        if (getArguments() != null && getArguments().containsKey("user")) {
            UserResponse user = (UserResponse) getArguments().getSerializable("user");
            if (user != null) {
                updateUI(user);
            } else {
                Toast.makeText(requireContext(), "Account information not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No account data available", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(UserResponse user) {
        binding.accountNameView.setText(user.getFullName());
        binding.id.setText(String.valueOf(user.getId()));
        binding.email.setText(user.getUserName());
        binding.address.setText(user.getLocation() != null ? user.getLocation() : "Address not updated");

        binding.employeeNumber.setText("4");
        binding.additionalInfo.setText("Our store is open every day of the week from 9 AM to 9 PM. We welcome you to come in and explore our wide selection of fresh and healthy food items. Whether you're looking for groceries or a quick snack, we've got something for everyone!");

        loadAvatar(user.getAvatarUrl());
    }

    private void loadAvatar(String url) {
        if (url != null && !url.trim().isEmpty()) {
            String fullUrl = Constants.URL_HOST_SERVER + url;
            Log.d("BranchInfoFragment", "Avatar URL: " + fullUrl);
            Glide.with(requireContext())
                    .load(AuthInterceptor.getAuthorizedGlideUrl(fullUrl))
                    .placeholder(R.drawable.avatar_default)
                    .error(R.drawable.avatar_default)
                    .into(binding.accountAvatarView);
        } else {
            binding.accountAvatarView.setImageResource(R.drawable.avatar_default);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
