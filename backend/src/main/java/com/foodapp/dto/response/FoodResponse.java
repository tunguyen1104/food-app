package com.foodapp.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String foodName;
    private String description;
    private Double price;
    private String categoryName;
    private String avatarUrl;
}
