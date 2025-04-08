package com.foodapp.dto.food;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {
    private Long id;
    private String name;
    private String imageUrl;
}
