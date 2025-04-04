package com.foodapp.controllers;

import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.Impl.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('MANAGER')")
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {

    OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrdersByBranch(@PathVariable Long userId) {
        var orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(orders)
                        .build()
        );
    }
}

