package com.example.foodapp.services;

import com.example.foodapp.dto.request.LoginRequest;
import com.example.foodapp.dto.request.RefreshTokenRequest;
import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/auth/signIn")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    @POST("/api/auth/refresh-token")
    Call<ApiResponse<AuthResponse>> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);
}
