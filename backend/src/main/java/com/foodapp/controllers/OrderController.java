package com.foodapp.controllers;

import com.foodapp.domain.Order;
import com.foodapp.dto.requests.OrderRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.dto.response.OrderResponse;
import com.foodapp.services.Impl.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId) {
        var orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(orders)
                        .build()
        );
    }

    @GetMapping("/history")
    public ResponseEntity<?> getMyOrderHistory() {
        var orders = orderService.getOrdersForCurrentUser();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(orders)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        var orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(orderResponse)
                        .build()
        );
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @Valid @RequestBody OrderRequest orderRequest) {
        var updatedOrder = orderService.updateOrder(orderId, orderRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(updatedOrder)
                        .build()
        );
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .build()
        );
    }

    @GetMapping("/status")
    public ResponseEntity<?> getMyOrdersByStatus(@RequestParam Order.Status status) {
        List<OrderResponse> orders = orderService.getMyOrdersByStatus(status);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(orders)
                        .build()
        );
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam("status") String statusStr) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, statusStr);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(updatedOrder)
                        .build()
        );
    }
}
