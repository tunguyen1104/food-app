package com.foodapp.services;

import com.foodapp.dto.statistic.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IStatisticService {
    List<MonthlyRevenueResponse> getMonthlyRevenue(int year, int month);
    List<FoodDistributionResponse> getFoodDistribution(int year, int month);
    List<OrderStatusDistributionResponse> getOrderStatusDistribution(int year, int month);
    List<PlatformRevenueResponse> getPlatformRevenue(int year, int month);
    List<CategorySalesResponse> getCategorySales(int year, int month);
    List<DailyVolumeResponse> getDailyVolume(int year, int month);
}
