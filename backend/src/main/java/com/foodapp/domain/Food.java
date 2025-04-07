package com.foodapp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food extends BaseEntity {
    private String foodName;
    private String description;
    private Double price;
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
