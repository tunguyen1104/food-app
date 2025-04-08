package com.example.foodapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.OrderDetailResponse;
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

    public interface OrderHistoryCallback {
        void onSuccess(List<OrderDetailResponse> orders);
        void onError(String message);
    }
}
