package com.example.foodapp.fragments.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodapp.databinding.FragmentSingleChatBinding;

public class SingleChatFragment extends Fragment {
    private FragmentSingleChatBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSingleChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
