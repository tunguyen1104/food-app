package com.foodapp.controllers;

import com.foodapp.dto.requests.OrderDetailRequest;
import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.Impl.OrderDetailService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderDetailController {

    OrderDetailService orderDetailService;

    @GetMapping("/{orderId}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        var detailResponses = orderDetailService.getDetailsByOrderId(orderId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(detailResponses)
                        .build()
        );
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getOrderDetailById(@PathVariable Long id) {
        var detail = orderDetailService.getDetailById(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(detail)
                        .build()
        );
    }

    @PostMapping("/details")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailRequest request) {
        var detail = orderDetailService.createOrderDetail(request);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(detail)
                        .build()
        );
    }

    @PutMapping("/details/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable Long id, @Valid @RequestBody OrderDetailRequest request) {
        var updated = orderDetailService.updateOrderDetail(id, request);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .data(updated)
                        .build()
        );
    }

    @DeleteMapping("/details/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(ApiResponse.Status.SUCCESS)
                        .build()
        );
    }
}
