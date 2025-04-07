package com.example.foodapp.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.repositories.OrderRepository;

import java.util.List;

public class OrderHistoryViewModel extends ViewModel {

    private final Context context;
    private final MutableLiveData<List<OrderResponse>> ordersLiveData = new MutableLiveData<>();
    private final OrderRepository orderRepository;

    public OrderHistoryViewModel(Context context) {
        this.context = context;
        this.orderRepository = new OrderRepository(context);
    }

    public LiveData<List<OrderResponse>> getOrders() {
        return ordersLiveData;
    }

    public void fetchOrders(Long userId) {
        orderRepository.getOrdersByUserId(userId, new OrderRepository.OrderHistoryCallback() {
            @Override
            public void onSuccess(List<OrderResponse> orders) {
                ordersLiveData.setValue(orders);
            }

            @Override
            public void onError(String message) {
                Log.e("OrderHistoryViewModel", "Error processing ID " + userId);
            }
        });
    }
}
