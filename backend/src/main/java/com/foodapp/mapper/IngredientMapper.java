package com.foodapp.mapper;

import com.foodapp.domain.Ingredient;
import com.foodapp.dto.food.IngredientDto;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {
    public IngredientDto toIngredientDto(Ingredient ingredient) {
        if (ingredient == null) return null;
        return IngredientDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .imageUrl(ingredient.getImageUrl())
                .build();
    }

    public Ingredient toIngredient(IngredientDto ingredientDto) {
        if (ingredientDto == null) return null;
        return Ingredient.builder()
                .name(ingredientDto.getName())
                .imageUrl(ingredientDto.getImageUrl())
                .build();
    }
}
