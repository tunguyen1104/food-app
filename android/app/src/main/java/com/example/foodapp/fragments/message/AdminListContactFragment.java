package com.example.foodapp.fragments.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodapp.databinding.FragmentContactListAdminBinding;

public class AdminListContactFragment extends Fragment {
    private FragmentContactListAdminBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentContactListAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
