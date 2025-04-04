package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_food_ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodIngredient extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Food food;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ingredient ingredient;
    private Long quantity;
}
