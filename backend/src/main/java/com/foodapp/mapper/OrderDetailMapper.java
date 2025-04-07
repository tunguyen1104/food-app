package com.foodapp.mapper;

import com.foodapp.domain.OrderDetail;
import com.foodapp.dto.response.OrderDetailResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {

    public OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .unitPrice(orderDetail.getFood() != null ? orderDetail.getFood().getPrice() : null)
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
