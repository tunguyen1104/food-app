package com.foodapp.controllers;

import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.Impl.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getMyOrderHistory() {
        var orders = orderService.getOrdersForCurrentUser();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(orders)
                        .build()
        );
    }
}
