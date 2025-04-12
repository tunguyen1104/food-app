package com.example.foodapp.fragments.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentFoodCreateBinding;
import com.example.foodapp.dto.response.CategoryDto;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.dto.response.IngredientDto;
import com.example.foodapp.utils.NavigationUtil;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.home.FoodViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddFoodFragment extends Fragment {
    private FragmentFoodCreateBinding binding;
    private FoodViewModel foodViewModel;
    private CategoryDto selectedCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFoodCreateBinding.inflate(inflater, container, false);
        foodViewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), FoodViewModel.class))
                .get(FoodViewModel.class);
        NavigationUtil.setupBackNavigation(this, binding.backButton);
        observeAddState();
        setupIngredients();
        setupCategoryDropdown();
        binding.addButton.setOnClickListener(v -> saveFoodItem());
        return binding.getRoot();
    }

    private void observeAddState() {
        foodViewModel.getAddSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                foodViewModel.getFoodData();
                Toast.makeText(requireContext(), "Food updated successfully", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });
        // Observe add error
        foodViewModel.getAddError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategoryDropdown() {
        foodViewModel.getFoodCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                // Filter out invalid categories
                List<CategoryDto> validCategories = categories.stream()
                        .filter(category -> category != null && category.getName() != null && !category.getName().trim().isEmpty())
                        .collect(Collectors.toList());

                if (validCategories.isEmpty()) {
                    Log.w("AddFoodFragment", "No valid categories found");
                    Toast.makeText(requireContext(), "No categories available", Toast.LENGTH_SHORT).show();
                    binding.category.setEnabled(false);
                    return;
                }

                // Create custom adapter for CategoryDto
                ArrayAdapter<CategoryDto> categoryAdapter = new ArrayAdapter<CategoryDto>(
                        requireContext(), android.R.layout.simple_dropdown_item_1line, validCategories) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view;
                        CategoryDto category = getItem(position);
                        textView.setText(category != null ? category.getName() : "");
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView textView = (TextView) view;
                        CategoryDto category = getItem(position);
                        textView.setText(category != null ? category.getName() : "");
                        return view;
                    }
                };

                binding.category.setAdapter(categoryAdapter);

                // Handle category selection
                binding.category.setOnItemClickListener((parent, view, position, id) -> {
                    selectedCategory = categoryAdapter.getItem(position);
                    if (selectedCategory != null) {
                        binding.category.setText(selectedCategory.getName(), false);
                        Log.d("AddFoodFragment", "Selected category: " + selectedCategory.getName() + ", ID: " + selectedCategory.getId());
                    } else {
                        binding.category.setText("", false);
                    }
                });
            } else {
                Log.w("AddFoodFragment", "Categories list is null");
                binding.category.setEnabled(false);
            }
        });
        foodViewModel.getAllFoodCategories();
    }

    private void setupIngredients() {
        foodViewModel.getFoodIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            if (ingredients != null) {
                binding.ingredientChipGroup.removeAllViews(); // Clear existing chips
                for (IngredientDto ingredient : ingredients) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(ingredient.getName());
                    chip.setTag(ingredient); // Store full IngredientDto
                    chip.setCheckable(true);
                    chip.setChipBackgroundColorResource(R.color.chip_background_selector);
                    chip.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_text_selector));
                    chip.setChipStrokeWidth(1f);
                    chip.setChipStrokeColorResource(R.color.highlight_darkest);
                    binding.ingredientChipGroup.addView(chip);
                }
            }
        });
        foodViewModel.getAllFoodIngredients();
    }

    private void saveFoodItem() {
        String name = binding.name.getText().toString().trim();
        String priceStr = binding.price.getText().toString().trim();
        String timeStr = binding.timeText.getText().toString().trim();
        String desc = binding.descriptionText.getText().toString().trim();
        String categoryName = binding.category.getText().toString().trim();

        // Validation
        if (name.isEmpty() || priceStr.isEmpty() || timeStr.isEmpty() || categoryName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCategory == null || !selectedCategory.getName().equals(categoryName)) {
            Toast.makeText(requireContext(), "Please select a valid category from the list", Toast.LENGTH_SHORT).show();
            return;
        }

        Double price;
        Integer time;
        try {
            price = Double.parseDouble(priceStr);
            time = Integer.parseInt(timeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid time format", Toast.LENGTH_SHORT).show();
            return;
        }

        List<IngredientDto> selectedIngredients = new ArrayList<>();
        for (int i = 0; i < binding.ingredientChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.ingredientChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedIngredients.add((IngredientDto) chip.getTag());
            }
        }
        if (selectedIngredients.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least 1 ingredient", Toast.LENGTH_SHORT).show();
            return;
        }

        FoodDto createFood = new FoodDto();
        createFood.setFoodName(name);
        createFood.setPrice(price);
        createFood.setTime(time);
        createFood.setDescription(desc);
        createFood.setCategoryName(selectedCategory.getName());
        createFood.setCategoryId(selectedCategory.getId()); // Assuming FoodDto has setCategoryId
        createFood.setAvatarUrl(null);
        createFood.setIngredients(selectedIngredients);

        foodViewModel.addFoodItem(createFood);
    }
}
