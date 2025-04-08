package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderService {
    @GET("/api/orders/{id}")
    Call<ApiResponse<List<OrderResponse>>> getOrdersByUserId(@Path("id") Long id);

    @GET("/api/orders/myHistory")
    Call<ApiResponse<List<OrderResponse>>> getMyOrderHistory();
}
