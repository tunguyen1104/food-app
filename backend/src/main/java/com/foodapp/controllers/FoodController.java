package com.foodapp.controllers;

import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.FoodResponse;
import com.foodapp.services.Impl.FoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodController {

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable Long id) {
        FoodResponse foodResponse = foodService.getFoodById(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(foodResponse)
                        .build());
    }
}
