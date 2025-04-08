package com.foodapp.dto.response;

import com.foodapp.domain.Order;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Order.Status status;
    private Date orderDate;
    private Order.Platform orderPlatform;
    private Double totalPrice;
    private String description;
    private int quantity;
}
