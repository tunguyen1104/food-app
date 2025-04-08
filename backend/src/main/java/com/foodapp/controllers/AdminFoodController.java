package com.foodapp.controllers;

import com.foodapp.dto.requests.FoodRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.FoodResponse;
import com.foodapp.services.Impl.FoodService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('MANAGER')")
@RestController
@RequestMapping("/api/admin/foods")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminFoodController {

    FoodService foodService;

    @GetMapping
    public ResponseEntity<?> getFoods() {
        List<FoodResponse> foods = foodService.getFoods();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(foods)
                        .build());
    }

    @PostMapping
    public ResponseEntity<?> createFood(@RequestBody @Valid FoodRequest foodRequest) {
        FoodResponse foodResponse = foodService.createFood(foodRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(foodResponse)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody @Valid FoodRequest foodRequest) {
        FoodResponse foodResponse = foodService.updateFood(id, foodRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(foodResponse)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .build());
    }
}
