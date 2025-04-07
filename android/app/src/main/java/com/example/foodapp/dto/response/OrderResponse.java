package com.example.foodapp.dto.response;

import com.example.foodapp.enums.OrderPlatform;
import com.example.foodapp.enums.OrderStatus;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private Date orderDate;
    private OrderPlatform orderPlatform;
    private Double totalPrice;
    private String description;
    private int quantity;
}
