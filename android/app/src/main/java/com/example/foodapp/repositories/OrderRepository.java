package com.example.foodapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.OrderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private final Context context;
    private final OrderService orderService;

    public OrderRepository(Context context) {
        this.context = context;
        this.orderService = ApiClient.getClient(context).create(OrderService.class);
    }

    public void getOrdersByUserId(Long userId, final OrderHistoryCallback callback) {
        Call<ApiResponse<List<OrderResponse>>> call = orderService.getOrdersByUserId(userId);
        call.enqueue(new Callback<ApiResponse<List<OrderResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<OrderResponse>>> call, @NonNull Response<ApiResponse<List<OrderResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Error loading orders");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<OrderResponse>>> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public interface OrderHistoryCallback {
        void onSuccess(List<OrderResponse> orders);
        void onError(String message);
    }

}
