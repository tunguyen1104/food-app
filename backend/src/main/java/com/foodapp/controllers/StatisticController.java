package com.foodapp.controllers;

import com.foodapp.services.IStatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {
    private final IStatisticService statisticService;
    @GetMapping("/revenue/monthly")
    public ResponseEntity<?> getMonthlyRevenue(@RequestParam int year, @RequestParam int month) {
        var revenue = statisticService.getMonthlyRevenue(year, month);
        return ResponseEntity.ok(revenue);
    }
    @GetMapping("/food-distribution")
    public ResponseEntity<?> getFoodDistribution(@RequestParam int year, @RequestParam int month) {
        var revenue = statisticService.getFoodDistribution(year, month);
        return ResponseEntity.ok(revenue);
    }
    @GetMapping("/status-distribution")
    public ResponseEntity<?> getStatusDistribution(@RequestParam int year, @RequestParam int month) {
        var revenue = statisticService.getOrderStatusDistribution(year, month);
        return ResponseEntity.ok(revenue);
    }
    @GetMapping("/platform-revenue")
    public ResponseEntity<?> getPlatformRevenue(@RequestParam int year, @RequestParam int month) {
        var revenue = statisticService.getPlatformRevenue(year, month);
        return ResponseEntity.ok(revenue);
    }
    @GetMapping("/category-sales")
    public ResponseEntity<?> getCategorySale(@RequestParam int year, @RequestParam int month) {
        var revenue = statisticService.getCategorySales(year, month);
        return ResponseEntity.ok(revenue);
    }
    @GetMapping("/daily-volume")
    public ResponseEntity<?> getDailyVolume(@RequestParam int year, @RequestParam int month) {
        var revenue = statisticService.getDailyVolume(year, month);
        return ResponseEntity.ok(revenue);
    }
}
