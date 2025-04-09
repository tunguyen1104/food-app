package com.example.foodapp.viewmodel.order;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.OrderDetailResponse;
import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.enums.OrderStatus;
import com.example.foodapp.repositories.OrderDetailRepository;

import java.util.List;

public class OrderDetailViewModel extends ViewModel {
    private final OrderDetailRepository orderDetailRepository;
    private final MutableLiveData<List<OrderDetailResponse>> orderDetails = new MutableLiveData<>();

    public OrderDetailViewModel(Context context) {
        this.orderDetailRepository = new OrderDetailRepository(context.getApplicationContext());
    }

    public LiveData<List<OrderDetailResponse>> getOrderDetails() {
        return orderDetails;
    }

    public void fetchOrderDetails(Long orderId) {
        orderDetailRepository.getOrdersByOrderId(orderId, new OrderDetailRepository.OrderHistoryCallback() {
            @Override
            public void onSuccess(List<OrderDetailResponse> details) {
                Log.d("OrderDetailViewModel", "Received items: " + details.size());
                orderDetails.setValue(details);
            }

            @Override
            public void onError(String message) {
                Log.e("OrderDetailViewModel", "Error processing ID " + orderId + ": " + message);
            }
        });
    }

    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        orderDetailRepository.updateOrderStatus(orderId, newStatus, new OrderDetailRepository.OrderUpdateCallback() {
            @Override
            public void onSuccess(OrderResponse updatedOrder) {
            }

            @Override
            public void onError(String message) {
                Log.e("OrderDetailViewModel", "Error processing ID " + orderId + ": " + message);
            }
        });
    }
}
