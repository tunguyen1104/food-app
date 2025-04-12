package com.example.foodapp.fragments.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.R;
import com.example.foodapp.adapters.FoodHomeAdapter;
import com.example.foodapp.databinding.FragmentHomeBinding;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.fragments.food.AddFoodFragment;
import com.example.foodapp.fragments.food.EditFoodFragment;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.home.FoodViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

// Todo: Render home data
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FoodHomeAdapter foodAdapter;
    private FoodViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), FoodViewModel.class))
                .get(FoodViewModel.class);
        setupRecyclerView();
        loadData();
        observeFoodData();
        binding.addNewFood.setOnClickListener(this::onAdd);
        return binding.getRoot();
    }

    private void onAdd(View view) {
        AddFoodFragment addFoodFragment = new AddFoodFragment();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.homeContainer, addFoodFragment).addToBackStack(null).commit();
    }

    private void setupRecyclerView() {
        foodAdapter = new FoodHomeAdapter(new ArrayList<>(), this::onEdit, this::onDelete); // Initially empty
        binding.foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.foodRecyclerView.setAdapter(foodAdapter);
    }

    public void onEdit(FoodDto foodDto) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("foodItem", foodDto);

        EditFoodFragment editFoodFragment = new EditFoodFragment();
        editFoodFragment.setArguments(bundle);
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.homeContainer, editFoodFragment).addToBackStack(null).commit();
    }

    public void onDelete(FoodDto foodDto) {
        AlertDialog alert = new AlertDialog.Builder(requireContext())
                .setTitle("Delete Food Item")
                .setMessage("Are you sure want to delete" + foodDto.getFoodName())
                .setPositiveButton("Delete", ((dialog, which) -> viewModel.deleteFoodItem(foodDto.getId())))
                .setNegativeButton("Cancel", null)
                .show();
        loadData();
    }

    private void loadData() {
        viewModel.getFoodData();
    }

    private void observeFoodData() {
        // Observe food data
        viewModel.getFoodData().observe(getViewLifecycleOwner(), foodItems -> {
            if (foodItems != null) {
                foodAdapter.updateData(foodItems);
            } else {
                Snackbar.make(binding.getRoot(), "Error loading data", Snackbar.LENGTH_LONG).show();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
        // Observe delete status
        viewModel.getDeleteSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "Food item deleted", Toast.LENGTH_SHORT).show();
                // Refresh list (assuming foodList is updated)
                foodAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "Food item deleted", Toast.LENGTH_SHORT).show();
                // Refresh list (assuming foodList is updated)
                foodAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getDeleteError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
