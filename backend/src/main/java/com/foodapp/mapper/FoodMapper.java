package com.foodapp.mapper;

import com.foodapp.domain.Food;
import com.foodapp.dto.food.FoodDto;
import com.foodapp.dto.response.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class FoodMapper {
    private final IngredientMapper ingredientMapper;

    public FoodResponse toFoodResponse(Food food) {
        if (food == null) return null;

        return FoodResponse.builder()
                .id(food.getId())
                .foodName(food.getFoodName())
                .avatarUrl(food.getAvatarUrl())
                .price(food.getPrice())
                .description(food.getDescription())
                .categoryName(food.getCategory().getName())
                .build();
    }

    public FoodDto toFoodDto(Food food) {
        if (food == null) return null;
        var ingredients = food.getIngredients()
                .stream()
                .map(ingredientMapper::toIngredientDto)
                .toList();
        return FoodDto.builder()
                .id(food.getId())
                .foodName(food.getFoodName())
                .imageUrl(food.getAvatarUrl())
                .price(BigDecimal.valueOf(food.getPrice()))
                .description(food.getDescription())
                .category(food.getCategory().getName())
                .ingredients(ingredients)
                .build();
    }
}
