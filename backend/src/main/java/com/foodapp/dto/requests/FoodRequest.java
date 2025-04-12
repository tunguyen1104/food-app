package com.foodapp.dto.requests;

import com.foodapp.dto.food.IngredientDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodRequest {
    @NotBlank(message = "Food name cannot be blank")
    @Size(min = 2, max = 100, message = "Food name should be between 2 and 100 characters")
    private String foodName;

    @Size(max = 500, message = "Description should be less than 500 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;
    @NotNull(message = "Category Name cannot be null")
    private String categoryName;

    private String avatarUrl;
    private List<IngredientDto> ingredients;
}