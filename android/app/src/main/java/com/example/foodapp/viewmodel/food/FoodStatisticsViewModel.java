package com.example.foodapp.viewmodel.food;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.CategorySalesResponse;
import com.example.foodapp.dto.response.FoodDistributionResponse;
import com.example.foodapp.repositories.StatisticsRepository;

import java.util.List;

public class FoodStatisticsViewModel extends ViewModel {
    private StatisticsRepository repository;

    public FoodStatisticsViewModel(Context context) {
        repository = new StatisticsRepository(context);
    }

    public LiveData<List<FoodDistributionResponse>> getFoodDistribution(int year, int month) {
        return repository.getFoodDistribution(year, month);
    }

    public LiveData<List<CategorySalesResponse>> getCategorySales(int year, int month) {
        return repository.getCategorySales(year, month);
    }
}
