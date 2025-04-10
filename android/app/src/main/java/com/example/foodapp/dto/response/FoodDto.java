package com.example.foodapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodDto {
    private Long id;
    private String foodName;
    private String description;
    private Double price;
    private String categoryName;
    private String avatarUrl;
}
