package com.example.foodapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.OrderDetailResponse;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.enums.OrderStatus;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.OrderDetailService;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailRepository {
    private final Context context;
    private final OrderDetailService orderDetailService;

    public OrderDetailRepository(Context context) {
        this.context = context;
        this.orderDetailService = ApiClient.getClient(context).create(OrderDetailService.class);
    }

    public void getOrdersByOrderId(Long orderId, final OrderDetailRepository.OrderHistoryCallback callback) {
        Call<ApiResponse<List<OrderDetailResponse>>> call = orderDetailService.getOrderDetailsByOrderId(orderId);
        call.enqueue(new Callback<ApiResponse<List<OrderDetailResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<OrderDetailResponse>>> call, @NonNull Response<ApiResponse<List<OrderDetailResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Error loading order detail list");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<OrderDetailResponse>>> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public void updateOrderStatus(Long orderId, OrderStatus newStatus, final OrderDetailRepository.OrderUpdateCallback callback) {
        Call<ApiResponse<OrderResponse>> call = orderDetailService.updateOrderStatus(orderId, newStatus.name());
        call.enqueue(new Callback<ApiResponse<OrderResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OrderResponse>> call,
                                   @NonNull Response<ApiResponse<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Error updating order status");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<OrderResponse>> call,
                                  @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public interface OrderHistoryCallback {
        void onSuccess(List<OrderDetailResponse> orders);
        void onError(String message);
    }

    public interface OrderUpdateCallback {
        void onSuccess(OrderResponse updatedOrder);
        void onError(String message);
    }
}
