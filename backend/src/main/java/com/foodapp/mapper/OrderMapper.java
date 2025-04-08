package com.foodapp.mapper;

import com.foodapp.domain.Order;
import com.foodapp.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) return null;

        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .orderPlatform(order.getOrderPlatform())
                .totalPrice(order.getTotalPrice())
                .orderDate(Date.from(order.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()))
                .description(order.getDescription())
                .quantity(order.getOrderDetails() != null ? order.getOrderDetails().size() : 0)
                .build();
    }
}
