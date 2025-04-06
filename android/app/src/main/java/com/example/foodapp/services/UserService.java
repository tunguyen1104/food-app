package com.example.foodapp.services;

import com.example.foodapp.dto.request.CreateUserRequest;
import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface UserService {

    @GET("/api/users/profile")
    Call<ApiResponse<UserResponse>> getUserProfile();

    @GET("/api/admin/users")
    Call<ApiResponse<List<UserResponse>>> getAllUsers();

    @POST("/api/admin/users")
    Call<ApiResponse<UserResponse>> createUser(@Body CreateUserRequest request);

    @PUT("/api/admin/users/{id}")
    Call<ApiResponse<UserResponse>> updateUser(@Path("id") Long id, @Body CreateUserRequest request);

    @DELETE("/api/admin/users/{id}")
    Call<ApiResponse<Void>> deleteUser(@Path("id") Long id);
}

