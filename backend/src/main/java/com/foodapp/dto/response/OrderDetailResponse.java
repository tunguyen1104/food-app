package com.foodapp.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Double unitPrice;
    private Integer quantity;
    private String foodName;
    private String categoryName;
    private String avatarUrl;
}
