package com.example.foodapp.fragments.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.databinding.FragmentFoodStatisticsBinding;
import com.example.foodapp.dto.response.CategorySalesResponse;
import com.example.foodapp.dto.response.FoodDistributionResponse;
import com.example.foodapp.viewmodel.BaseViewModelFactory;
import com.example.foodapp.viewmodel.food.FoodStatisticsViewModel;
import com.example.foodapp.viewmodel.util.DateSelectionViewModel;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class FoodStatisticsFragment extends Fragment {
    private FragmentFoodStatisticsBinding binding;
    private FoodStatisticsViewModel viewModel;
    private DateSelectionViewModel dateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), FoodStatisticsViewModel.class))
                .get(FoodStatisticsViewModel.class);
        dateViewModel = new ViewModelProvider(this, new BaseViewModelFactory<>(requireContext(), DateSelectionViewModel.class))
                .get(DateSelectionViewModel.class);

        setupCharts();
        observeDateChanges();
    }

    private void setupCharts() {
        binding.foodPieChart.getDescription().setEnabled(false);
        binding.foodPieChart.setUsePercentValues(true);
        binding.foodPieChart.setDrawHoleEnabled(true);

        binding.categoryBarChart.getDescription().setEnabled(false);
        binding.categoryBarChart.setFitBars(true);
    }

    private void observeDateChanges() {
        dateViewModel.getSelectedYear().observe(getViewLifecycleOwner(), year -> {
            Integer month = dateViewModel.getSelectedMonth().getValue();
            if (month != null) {
                loadData(year, month);
            }
        });

        dateViewModel.getSelectedMonth().observe(getViewLifecycleOwner(), month -> {
            Integer year = dateViewModel.getSelectedYear().getValue();
            if (year != null) {
                loadData(year, month);
            }
        });
    }

    private void loadData(int year, int month) {
        viewModel.getFoodDistribution(year, month).observe(getViewLifecycleOwner(), foodResponses -> {
            if (foodResponses != null) {
                updateFoodChart(foodResponses);
            }
        });

        viewModel.getCategorySales(year, month).observe(getViewLifecycleOwner(), categoryResponses -> {
            if (categoryResponses != null) {
                updateCategoryChart(categoryResponses);
            }
        });
    }

    private void updateFoodChart(List<FoodDistributionResponse> foodResponses) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (FoodDistributionResponse response : foodResponses) {
            entries.add(new PieEntry(response.getQuantity(), response.getFoodName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Food Items");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        binding.foodPieChart.setData(pieData);
        binding.foodPieChart.invalidate();
    }

    private void updateCategoryChart(List<CategorySalesResponse> categoryResponses) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < categoryResponses.size(); i++) {
            CategorySalesResponse response = categoryResponses.get(i);
            entries.add(new BarEntry(i, response.getQuantity()));
            labels.add(response.getCategoryName());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Category Sales");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        binding.categoryBarChart.setData(barData);
        binding.categoryBarChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels));
        binding.categoryBarChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
