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
import com.example.foodapp.databinding.FragmentFoodEditorBinding;
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

public class EditFoodFragment extends Fragment {
    private Long foodId;
    private FoodDto foodItem;
    private FragmentFoodEditorBinding binding;
    private FoodViewModel foodViewModel;
    private CategoryDto selectedCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFoodEditorBinding.inflate(inflater, container, false);
        foodViewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), FoodViewModel.class))
                .get(FoodViewModel.class);
        NavigationUtil.setupBackNavigation(this, binding.backButton);
        if (getArguments() != null) {
            foodItem = getArguments().getParcelable("foodItem");
            if (foodItem != null) {
                foodId = foodItem.getId();
                foodViewModel.getFoodDetailById(foodId);
            }
        }
        // Setup category dropdown dynamically
        observeFoodData();
        binding.addButton.setOnClickListener(v -> saveFoodItem());
        return binding.getRoot();
    }

    private void observeFoodData() {
        foodViewModel.getFoodDetail().observe(getViewLifecycleOwner(), foodDto -> {
            if (foodDto != null) {
                populateFields(foodDto);
                Log.d("FoodFragment", "Food detail: " + foodDto.getFoodName());
            }
        });
        // Observe update success
        foodViewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                foodViewModel.getFoodData();
                Toast.makeText(requireContext(), "Food updated successfully", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });

        // Observe update error
        foodViewModel.getUpdateError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields(FoodDto foodDto) {
        setupCategoryDropdown();
        setupIngredients();
        binding.name.setText(foodDto.getFoodName());
        binding.price.setText(String.valueOf(foodDto.getPrice()));
        binding.id.setText(String.valueOf(foodDto.getId()));
        binding.timeText.setText(String.valueOf(foodDto.getTime()));
        binding.descriptionText.setText(foodDto.getDescription());
        if (foodDto.getIngredients() != null) {
            for (int i = 0; i < binding.ingredientChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.ingredientChipGroup.getChildAt(i);
                IngredientDto chipIngredient = (IngredientDto) chip.getTag();
                chip.setChecked(foodDto.getIngredients().stream()
                        .anyMatch(ingredient -> ingredient.getName().equals(chipIngredient.getName())));
            }
        }
        // Pre-set selectedCategory if available
        if (foodDto.getCategoryName() != null) {
            foodViewModel.getFoodCategories().observe(getViewLifecycleOwner(), categories -> {
                if (categories != null) {
                    for (CategoryDto category : categories) {
                        if (category != null && foodDto.getCategoryName().equals(category.getName())) {
                            selectedCategory = category;
                            break;
                        }
                    }
                }
            });
        }
    }

    private void setupCategoryDropdown() {
        foodViewModel.getFoodCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                // Filter out invalid categories
                List<CategoryDto> validCategories = categories.stream()
                        .filter(category -> category != null && category.getName() != null && !category.getName().trim().isEmpty())
                        .collect(Collectors.toList());

                if (validCategories.isEmpty()) {
                    Log.w("EditFoodFragment", "No valid categories found");
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

                // Set current category if editing
                if (foodItem != null && foodItem.getCategoryName() != null) {
                    for (int i = 0; i < validCategories.size(); i++) {
                        if (foodItem.getCategoryName().equals(validCategories.get(i).getName())) {
                            binding.category.setText(validCategories.get(i).getName(), false);
                            selectedCategory = validCategories.get(i);
                            break;
                        }
                    }
                }

                // Handle category selection
                binding.category.setOnItemClickListener((parent, view, position, id) -> {
                    selectedCategory = categoryAdapter.getItem(position);
                    if (selectedCategory != null) {
                        binding.category.setText(selectedCategory.getName(), false);
                        Log.d("EditFoodFragment", "Selected category: " + selectedCategory.getName() + ", ID: " + selectedCategory.getId());
                    } else {
                        selectedCategory = null;
                        binding.category.setText("", false);
                    }
                });
            } else {
                Log.w("EditFoodFragment", "Categories list is null");
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

        FoodDto updatedFood = new FoodDto();
        updatedFood.setId(foodItem != null ? foodItem.getId() : null);
        updatedFood.setFoodName(name);
        updatedFood.setPrice(price);
        updatedFood.setTime(time);
        updatedFood.setDescription(desc);
        updatedFood.setCategoryName(selectedCategory.getName());
        updatedFood.setCategoryId(selectedCategory.getId()); // Assuming FoodDto has setCategoryId
        updatedFood.setAvatarUrl(foodItem != null ? foodItem.getAvatarUrl() : null);
        updatedFood.setIngredients(selectedIngredients);

        foodViewModel.updateFoodItem(updatedFood);
    }
}
