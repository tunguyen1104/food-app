package com.example.foodapp.viewmodel.account;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.OrderResponse;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.repositories.OrderRepository;
import com.example.foodapp.repositories.UserRepository;

import java.util.List;

public class AccountViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<UserResponse> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<OrderResponse>> ordersLiveData = new MutableLiveData<>();
    private final OrderRepository orderRepository;

    public AccountViewModel(Context context) {
        userRepository = new UserRepository(context.getApplicationContext());
        orderRepository = new OrderRepository(context.getApplicationContext());
    }

    public LiveData<UserResponse> getUserProfile() {
        return userLiveData;
    }

    public void fetchUserProfile() {
        userRepository.getUserProfile(new UserRepository.UserProfileCallback() {
            @Override
            public void onSuccess(UserResponse user) {
                Log.d("AccountViewModel", "User profile loaded: " + user.getFullName());
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String message) {
                Log.e("AccountViewModel", "Failed to load user profile: " + message);
            }
        });
    }

    public void getMyOrderHistory() {
        orderRepository.getMyOrderHistory(new OrderRepository.OrderHistoryCallback() {
            @Override
            public void onSuccess(List<OrderResponse> orders) {
                ordersLiveData.setValue(orders);
            }

            @Override
            public void onError(String message) {
                Log.e("OrderHistoryViewModel", "Error processing ID ");
            }
        });
    }
}
