package com.example.foodapp.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.adapters.FoodCreateOrderAdapter;
import com.example.foodapp.databinding.FragmentCreateOrderBinding;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.listeners.CreateOrderAdapterListener;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.home.FoodViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CreateOrderFragment extends Fragment {
    private FragmentCreateOrderBinding binding;
    private FoodCreateOrderAdapter adapter;
    private FoodViewModel foodViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateOrderBinding.inflate(inflater, container, false);
        foodViewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), FoodViewModel.class))
                .get(FoodViewModel.class);
        setupRecyclerView();
        observeFoodData();
        loadData();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new FoodCreateOrderAdapter(new ArrayList<>(), new CreateOrderAdapterListener() {
            @Override
            public void onItemCheckedChange(FoodDto item, boolean isChecked) {

            }

            @Override
            public void onQuantityChange(FoodDto item, int quantity) {

            }
        });
        binding.foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.foodRecyclerView.setAdapter(adapter);
    }

    private void loadData() {
        foodViewModel.getFoodData();
    }

    private void observeFoodData() {
        // Observe food data
        foodViewModel.getFoodData().observe(getViewLifecycleOwner(), foodItems -> {
            if (foodItems != null) {
                adapter.updateData(foodItems);
                Snackbar.make(binding.getRoot(), "Loading Successfully", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(binding.getRoot(), "Error loading data", Snackbar.LENGTH_LONG).show();
            }
        });

        // Observe error messages
        foodViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
