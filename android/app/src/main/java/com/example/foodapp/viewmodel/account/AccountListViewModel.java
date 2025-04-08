package com.example.foodapp.viewmodel.account;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.repositories.UserRepository;

import java.util.List;

public class AccountListViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<List<UserResponse>> userListLiveData = new MutableLiveData<>();

    public AccountListViewModel(Context context) {
        this.userRepository = new UserRepository(context.getApplicationContext());
    }

    public LiveData<List<UserResponse>> getUserList() {
        return userListLiveData;
    }

    public void fetchAllUsers() {
        userRepository.getAllUsers(new UserRepository.UserListCallback() {
            @Override
            public void onSuccess(List<UserResponse> users) {
                userListLiveData.setValue(users);
            }

            @Override
            public void onError(String message) {
                Log.e("AccountListViewModel", "Failed to load users: " + message);
                userListLiveData.setValue(null); // Optionally clear or keep previous state
            }
        });
    }
}
