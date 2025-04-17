package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.dto.statistic.*;
import com.foodapp.exceptions.AppException;
import com.foodapp.repositories.FoodRepository;
import com.foodapp.repositories.OrderRepository;
import com.foodapp.services.IStatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class StatisticService implements IStatisticService {
    static final Logger logger = LoggerFactory.getLogger(StatisticService.class);
    OrderRepository orderRepository;
    @Override
    @Transactional(readOnly = true)
    public List<MonthlyRevenueResponse> getMonthlyRevenue(int year, int month) {
        validateDate(year, month);

        YearMonth yearMonth = YearMonth.of(year,month);
        Timestamp startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

        var results = orderRepository.findMonthlyRevenue(startDate,endDate);
        var response = results.stream()
                .map(result -> {
                    String monthName = yearMonth.getMonth().getDisplayName(TextStyle.FULL, java.util.Locale.getDefault());
                    Double revenue = (Double) result[0];
                    return MonthlyRevenueResponse.builder()
                            .month(monthName)
                            .revenue(revenue)
                            .build();
                }).toList();
        return Collections.unmodifiableList(response);
    }

    @Override
    public List<FoodDistributionResponse> getFoodDistribution(int year, int month) {
        validateDate(year, month);

        YearMonth yearMonth = YearMonth.of(year,month);
        Timestamp startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

        var results = orderRepository.findFoodDistribution(startDate,endDate);
        var response = results.stream()
                .map(result -> {
                    String foodName = String.valueOf(result[0]);
                    Long quantity = (Long) result[1];
                    return FoodDistributionResponse.builder()
                            .foodName(foodName)
                            .quantity(quantity)
                            .build();
                }).toList();
        return Collections.unmodifiableList(response);
    }

    @Override
    public List<OrderStatusDistributionResponse> getOrderStatusDistribution(int year, int month) {
        validateDate(year, month);

        YearMonth yearMonth = YearMonth.of(year,month);
        Timestamp startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

        var results = orderRepository.findOrderStatusDistribution(startDate,endDate);
        var response = results.stream()
                .map(result -> {
                    String status = String.valueOf(result[0]);
                    Long quantity = (Long) result[1];
                    return OrderStatusDistributionResponse.builder()
                            .status(status)
                            .count(quantity)
                            .build();
                }).toList();
        return Collections.unmodifiableList(response);
    }

    @Override
    public List<PlatformRevenueResponse> getPlatformRevenue(int year, int month) {
        validateDate(year, month);

        YearMonth yearMonth = YearMonth.of(year,month);
        Timestamp startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

        var results = orderRepository.findPlatformRevenue(startDate,endDate);
        var response = results.stream()
                .map(result -> {
                    Double quantity = (Double) result[0];
                    String platform = String.valueOf(result[1]);
                    return PlatformRevenueResponse.builder()
                            .platform(platform)
                            .revenue(quantity)
                            .build();
                }).toList();
        return Collections.unmodifiableList(response);
    }

    @Override
    public List<CategorySalesResponse> getCategorySales(int year, int month) {
        validateDate(year, month);

        YearMonth yearMonth = YearMonth.of(year,month);
        Timestamp startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

        var results = orderRepository.findCategorySales(startDate,endDate);
        var response = results.stream()
                .map(result -> {
                    String categoryName = String.valueOf(result[0]);
                    Long quantity = (Long) result[1];
                    return CategorySalesResponse.builder()
                            .categoryName(categoryName)
                            .quantity(quantity)
                            .build();
                }).toList();
        return Collections.unmodifiableList(response);
    }

    @Override
    public List<DailyVolumeResponse> getDailyVolume(int year, int month) {
        validateDate(year, month);

        YearMonth yearMonth = YearMonth.of(year,month);
        Timestamp startDate = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));

        var results = orderRepository.getDailyVolume(startDate,endDate);
        var response = results.stream()
                .map(result -> {
                    int day = (int) result[0];
                    Long quantity = (Long) result[1];
                    return DailyVolumeResponse.builder()
                            .day(day)
                            .orderCount(quantity)
                            .build();
                }).toList();
        return Collections.unmodifiableList(response);
    }
    private static void validateDate(int year, int month) {
        if (year < 1990 || year > LocalDate.now().getYear()) {
            logger.error("Invalid year: {}", year);
            throw new AppException(ErrorCode.INVALID_YEAR_REQUEST);
        }
        if (month < 1 || month > 12){
            logger.error("Invalid month: {}", month);
            throw new AppException(ErrorCode.INVALID_MONTH_REQUEST);
        }
        logger.info("Getting monthly revenue for year: {} and month: {}", year, month);
    }
}
