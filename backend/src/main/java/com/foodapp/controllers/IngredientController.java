package com.foodapp.controllers;

import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.Impl.IngredientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IngredientController {
    IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<?> getAllIngredients() {
        return ResponseEntity.ok(ApiResponse.builder()
                .status(ApiResponse.Status.SUCCESS)
                .data(ingredientService.getAllFoodIngredients())
                .build());
    }
}
