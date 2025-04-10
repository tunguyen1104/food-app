package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.OrderDetailResponse;
import com.example.foodapp.dto.response.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderDetailService {
    @GET("/api/orders/{id}/details")
    Call<ApiResponse<List<OrderDetailResponse>>> getOrderDetailsByOrderId(@Path("id") Long id);

    @PATCH("/api/orders/{orderId}/status")
    Call<ApiResponse<OrderResponse>> updateOrderStatus(
            @Path("orderId") Long orderId,
            @Query("status") String status
    );
}
