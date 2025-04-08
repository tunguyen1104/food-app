package com.foodapp.mapper;

import com.foodapp.domain.Food;
import com.foodapp.dto.response.FoodResponse;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

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
}
