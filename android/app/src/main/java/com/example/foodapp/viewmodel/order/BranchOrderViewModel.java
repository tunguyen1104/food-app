package com.example.foodapp.viewmodel.order;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.enums.OrderStatus;
import com.example.foodapp.repositories.OrderRepository;

import java.util.List;

public class BranchOrderViewModel extends ViewModel {
    private final OrderRepository orderRepository;
    private final MutableLiveData<List<OrderResponse>> ordersLiveData = new MutableLiveData<>();

    public BranchOrderViewModel(Context context) {
        orderRepository = new OrderRepository(context.getApplicationContext());
    }

    public LiveData<List<OrderResponse>> getOrders() {
        return ordersLiveData;
    }

    public void fetchOrdersByStatus(OrderStatus status) {
        orderRepository.getMyOrdersByStatus(status, new OrderRepository.OrderListCallback() {
            @Override
            public void onSuccess(List<OrderResponse> orders) {
                ordersLiveData.setValue(orders);
            }

            @Override
            public void onError(String message) {
                ordersLiveData.setValue(null);
            }
        });
    }
}
