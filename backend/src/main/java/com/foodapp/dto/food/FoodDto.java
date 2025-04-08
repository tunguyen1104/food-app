package com.foodapp.dto.food;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodDto {
    private Long id;
    private String foodName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private List<IngredientDto> ingredients;
}
