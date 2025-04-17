package com.example.foodapp.viewmodel.order;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.DailyVolumeResponse;
import com.example.foodapp.dto.response.MonthlyRevenueResponse;
import com.example.foodapp.dto.response.OrderStatusResponse;
import com.example.foodapp.dto.response.PlatformRevenueResponse;
import com.example.foodapp.repositories.StatisticsRepository;

import java.util.List;

public class OrderStatisticsViewModel extends ViewModel {
    private final StatisticsRepository repository;

    public OrderStatisticsViewModel(Context context) {
        repository = new StatisticsRepository(context);
    }

    public LiveData<List<MonthlyRevenueResponse>> getMonthlyRevenue(int year, int month) {
        return repository.getMonthlyRevenue(year, month);
    }

    public LiveData<List<OrderStatusResponse>> getOrderStatusDistribution(int year, int month) {
        return repository.getOrderStatusDistribution(year, month);
    }

    public LiveData<List<PlatformRevenueResponse>> getPlatformRevenue(int year, int month) {
        return repository.getPlatformRevenue(year, month);
    }

    public LiveData<List<DailyVolumeResponse>> getDailyVolume(int year, int month) {
        return repository.getDailyVolume(year, month);
    }
}
