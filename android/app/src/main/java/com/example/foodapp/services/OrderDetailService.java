package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.OrderDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderDetailService {
    @GET("/api/orders/{id}/details")
    Call<ApiResponse<List<OrderDetailResponse>>> getOrderDetailsByOrderId(@Path("id") Long id);
}
