package com.foodapp.controllers;

import com.foodapp.services.IFoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodController {
    IFoodService foodService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllFood() {
        var data = foodService.getAllFoodByCategory();
        return ResponseEntity.ok(data);
    }
}
