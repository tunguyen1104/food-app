package com.example.foodapp.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodapp.databinding.FragmentAccountAdminBinding;

public class AdminAccountFragment extends Fragment {
    private FragmentAccountAdminBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}