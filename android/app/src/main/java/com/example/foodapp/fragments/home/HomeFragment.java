package com.example.foodapp.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.adapters.FoodHomeAdapter;
import com.example.foodapp.databinding.FragmentHomeBinding;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.home.HomeViewModel;

import java.util.ArrayList;

// Todo: Render home data
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FoodHomeAdapter foodAdapter;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), HomeViewModel.class))
                .get(HomeViewModel.class);
        setupRecyclerView();
        setupViewModel();
        viewModel.getFoodData();
    }

    private void setupRecyclerView() {
        foodAdapter = new FoodHomeAdapter(new ArrayList<>()); // Initially empty
        binding.foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.foodRecyclerView.setAdapter(foodAdapter);

    }

    private void setupViewModel() {
        // Observe food data
        viewModel.getFoodData().observe(getViewLifecycleOwner(), foodItems -> {
            if (foodItems != null) {
                foodAdapter.updateData(foodItems);
            } else {
                System.out.println("Error loading data");
                Toast.makeText(getContext(), "Loading...", Toast.LENGTH_LONG).show();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
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
