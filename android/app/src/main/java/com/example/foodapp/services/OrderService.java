package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.enums.OrderStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    @GET("/api/orders/{id}")
    Call<ApiResponse<List<OrderResponse>>> getOrdersByUserId(@Path("id") Long id);

    @GET("/api/orders")
    Call<ApiResponse<List<OrderResponse>>> getMyOrderHistory();

    @GET("/api/orders/status")
    Call<ApiResponse<List<OrderResponse>>> getMyOrdersByStatus(
            @Query("status") OrderStatus status
    );
}
