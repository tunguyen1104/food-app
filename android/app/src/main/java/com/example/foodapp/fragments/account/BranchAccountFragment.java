package com.example.foodapp.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodapp.databinding.FragmentAccountBranchBinding;

public class BranchAccountFragment extends Fragment {
    private FragmentAccountBranchBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountBranchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
