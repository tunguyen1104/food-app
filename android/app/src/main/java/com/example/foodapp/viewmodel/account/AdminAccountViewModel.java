package com.example.foodapp.viewmodel.account;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.repositories.UserRepository;

public class AdminAccountViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<UserResponse> userLiveData = new MutableLiveData<>();

    public AdminAccountViewModel(Context context) {
        this.userRepository = new UserRepository(context.getApplicationContext());
    }

    public LiveData<UserResponse> getUser() {
        return userLiveData;
    }

    public void fetchUserProfile() {
        userRepository.getUserProfile(new UserRepository.UserProfileCallback() {
            @Override
            public void onSuccess(UserResponse user) {
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String message) {
                Log.e("AdminAccountViewModel", "Failed to load user: " + message);
            }
        });
    }
}
