package com.example.foodapp.services;

import com.example.foodapp.dto.response.CategorySalesResponse;
import com.example.foodapp.dto.response.DailyVolumeResponse;
import com.example.foodapp.dto.response.FoodDistributionResponse;
import com.example.foodapp.dto.response.MonthlyRevenueResponse;
import com.example.foodapp.dto.response.OrderStatusResponse;
import com.example.foodapp.dto.response.PlatformRevenueResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StatisticsService {
    @GET("/api/orders/revenue/monthly")
    Call<List<MonthlyRevenueResponse>> getMonthlyRevenue(
            @Query("year") int year,
            @Query("month") int month
    );

    @GET("/api/orders/food-distribution")
    Call<List<FoodDistributionResponse>> getFoodDistribution(
            @Query("year") int year,
            @Query("month") int month
    );

    @GET("/api/orders/status-distribution")
    Call<List<OrderStatusResponse>> getOrderStatusDistribution(
            @Query("year") int year,
            @Query("month") int month
    );

    @GET("/api/orders/platform-revenue")
    Call<List<PlatformRevenueResponse>> getPlatformRevenue(
            @Query("year") int year,
            @Query("month") int month
    );

    @GET("/api/orders/category-sales")
    Call<List<CategorySalesResponse>> getCategorySales(
            @Query("year") int year,
            @Query("month") int month
    );

    @GET("/api/orders/daily-volume")
    Call<List<DailyVolumeResponse>> getDailyVolume(
            @Query("year") int year,
            @Query("month") int month
    );
}
