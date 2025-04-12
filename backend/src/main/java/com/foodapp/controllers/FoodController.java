package com.foodapp.controllers;

import com.foodapp.dto.food.FoodDto;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.FoodResponse;
import com.foodapp.services.Impl.FoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodController {
    Logger logger = LogManager.getLogger(FoodController.class);
    FoodService foodService;

    @GetMapping
    public ResponseEntity<?> getFoodsByBranch() {
        List<FoodResponse> foods = foodService.getFoods();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(foods)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody FoodDto food) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data("")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable Long id) {
        var foodResponse = foodService.getFoodById(id);
        logger.info("getFoodById: " + id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(foodResponse)
                        .build());
    }
}
