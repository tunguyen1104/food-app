package com.example.foodapp.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.dto.response.CategorySalesResponse;
import com.example.foodapp.dto.response.DailyVolumeResponse;
import com.example.foodapp.dto.response.FoodDistributionResponse;
import com.example.foodapp.dto.response.MonthlyRevenueResponse;
import com.example.foodapp.dto.response.OrderStatusResponse;
import com.example.foodapp.dto.response.PlatformRevenueResponse;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.StatisticsService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsRepository {
    private final StatisticsService statisticsService;

    public StatisticsRepository(Context context) {
        this.statisticsService = ApiClient.getClient(context).create(StatisticsService.class);
    }

    public LiveData<List<MonthlyRevenueResponse>> getMonthlyRevenue(int year, int month) {
        MutableLiveData<List<MonthlyRevenueResponse>> revenueData = new MutableLiveData<>();
        statisticsService.getMonthlyRevenue(year, month).enqueue(new Callback<List<MonthlyRevenueResponse>>() {
            @Override
            public void onResponse(Call<List<MonthlyRevenueResponse>> call, Response<List<MonthlyRevenueResponse>> response) {
                if (response.isSuccessful()) {
                    revenueData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MonthlyRevenueResponse>> call, Throwable t) {
                revenueData.setValue(null);
            }
        });
        return revenueData;
    }

    public LiveData<List<FoodDistributionResponse>> getFoodDistribution(int year, int month) {
        MutableLiveData<List<FoodDistributionResponse>> foodData = new MutableLiveData<>();
        statisticsService.getFoodDistribution(year, month).enqueue(new Callback<List<FoodDistributionResponse>>() {
            @Override
            public void onResponse(Call<List<FoodDistributionResponse>> call, Response<List<FoodDistributionResponse>> response) {
                if (response.isSuccessful()) {
                    foodData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<FoodDistributionResponse>> call, Throwable t) {
                foodData.setValue(null);
            }
        });
        return foodData;
    }

    public LiveData<List<OrderStatusResponse>> getOrderStatusDistribution(int year, int month) {
        MutableLiveData<List<OrderStatusResponse>> statusData = new MutableLiveData<>();
        statisticsService.getOrderStatusDistribution(year, month).enqueue(new Callback<List<OrderStatusResponse>>() {
            @Override
            public void onResponse(Call<List<OrderStatusResponse>> call, Response<List<OrderStatusResponse>> response) {
                if (response.isSuccessful()) {
                    statusData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<OrderStatusResponse>> call, Throwable t) {
                statusData.setValue(null);
            }
        });
        return statusData;
    }

    public LiveData<List<PlatformRevenueResponse>> getPlatformRevenue(int year, int month) {
        MutableLiveData<List<PlatformRevenueResponse>> platformData = new MutableLiveData<>();
        statisticsService.getPlatformRevenue(year, month).enqueue(new Callback<List<PlatformRevenueResponse>>() {
            @Override
            public void onResponse(Call<List<PlatformRevenueResponse>> call, Response<List<PlatformRevenueResponse>> response) {
                if (response.isSuccessful()) {
                    platformData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PlatformRevenueResponse>> call, Throwable t) {
                platformData.setValue(null);
            }
        });
        return platformData;
    }

    public LiveData<List<CategorySalesResponse>> getCategorySales(int year, int month) {
        MutableLiveData<List<CategorySalesResponse>> categoryData = new MutableLiveData<>();
        statisticsService.getCategorySales(year, month).enqueue(new Callback<List<CategorySalesResponse>>() {
            @Override
            public void onResponse(Call<List<CategorySalesResponse>> call, Response<List<CategorySalesResponse>> response) {
                if (response.isSuccessful()) {
                    categoryData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CategorySalesResponse>> call, Throwable t) {
                categoryData.setValue(null);
            }
        });
        return categoryData;
    }

    public LiveData<List<DailyVolumeResponse>> getDailyVolume(int year, int month) {
        MutableLiveData<List<DailyVolumeResponse>> volumeData = new MutableLiveData<>();
        statisticsService.getDailyVolume(year, month).enqueue(new Callback<List<DailyVolumeResponse>>() {
            @Override
            public void onResponse(Call<List<DailyVolumeResponse>> call, Response<List<DailyVolumeResponse>> response) {
                if (response.isSuccessful()) {
                    volumeData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<DailyVolumeResponse>> call, Throwable t) {
                volumeData.setValue(null);
            }
        });
        return volumeData;
    }
}
