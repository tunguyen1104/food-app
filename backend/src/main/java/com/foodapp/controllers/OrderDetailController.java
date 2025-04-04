package com.foodapp.controllers;

import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.Impl.OrderDetailService;
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
}
