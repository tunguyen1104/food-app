package com.example.foodapp.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodapp.databinding.FragmentCreateOrderBinding;

public class CreateOrderFragment extends Fragment {
    private FragmentCreateOrderBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
