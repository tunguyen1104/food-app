package com.foodapp.dto.requests;

import com.foodapp.domain.Order;
import com.foodapp.validator.EnumValidator;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull(message = "Status cannot be null")
    @EnumValidator(enumClass = Order.Status.class, message = "Invalid status value")
    private String status;

    @NotNull(message = "Order platform cannot be null")
    @EnumValidator(enumClass = Order.Platform.class, message = "Invalid platform value")
    private String orderPlatform;

    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private Double totalPrice;

    @Size(max = 500, message = "Description should be less than 500 characters")
    private String description;

    @NotNull(message = "Order details cannot be null")
    @Size(min = 1, message = "Order must contain at least one item")
    private List<OrderDetailRequest> orderDetails;
}
