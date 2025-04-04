package com.foodapp.mapper;

import com.foodapp.domain.Order;
import com.foodapp.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderResponse toOrderResponse(Order order) {
        if (order == null) return null;

        return OrderResponse.builder()
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .orderPlatform(order.getOrderPlatform())
                .totalPrice(order.getTotalPrice())
                .orderTime(order.getOrderTime())
                .build();
    }
}
