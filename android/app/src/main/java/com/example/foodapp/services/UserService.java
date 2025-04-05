package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.*;

public interface UserService {

    @GET("/api/users/profile")
    Call<ApiResponse<UserResponse>> getUserProfile();
}

