package com.foodapp.mapper;

import com.foodapp.domain.OrderDetail;
import com.foodapp.dto.response.OrderDetailResponse;

public class OrderDetailMapper {

    public static OrderDetailResponse toResponse(OrderDetail detail) {
        return OrderDetailResponse.builder()
                .id(detail.getId())
                .unitPrice(detail.getUnitPrice())
                .quantity(detail.getQuantity())
                .build();
    }
}
